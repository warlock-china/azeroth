package cn.com.warlock.emitter.zk;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.warlock.emitter.zk.connection.ZooKeeperConnection;
import cn.com.warlock.emitter.zk.connection.ZooKeeperConnectionObserver;

public class ResourceClaim implements ZooKeeperConnectionObserver, Closeable {
    final static Logger       logger         = LoggerFactory.getLogger(ResourceClaim.class);

    static String             ZNODE;
    static String             QUEUE_NODE;
    static String             POOL_NODE;
    final static String       LOCKING_TICKET = "nr-00000000000000";

    static final Random       random         = new Random();
    final int                 resource;

    final int                 poolSize;
    final ZooKeeper           zookeeper;
    final ZooKeeperConnection zooKeeperConnection;

    protected State           state          = State.UNCLAIMED;

    ResourceClaim(ZooKeeperConnection zooKeeperConnection, int poolSize,
                  String znode) throws IOException {
        logger.debug("Acquiring resource-claim…");

        ZNODE = znode;
        QUEUE_NODE = znode + "/queue";
        POOL_NODE = znode + "/pool";
        zooKeeperConnection.registerObserver(this);
        this.poolSize = poolSize;
        this.zooKeeperConnection = zooKeeperConnection;
        this.zookeeper = zooKeeperConnection.get();

        if (zookeeper.getState() != ZooKeeper.States.CONNECTED) {
            throw new IOException("Not connected to ZooKeeper quorum.");
        }

        try {
            ensureRequiredZnodesExist(zookeeper, znode);
            String placeInLine = acquireLock(zookeeper, QUEUE_NODE);
            this.resource = claimResource(zookeeper, POOL_NODE, poolSize);
            releaseTicket(zookeeper, QUEUE_NODE, placeInLine);
        } catch (KeeperException e) {
            throw new IOException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }
        state = State.HAS_CLAIM;

        logger.debug("Resource-claim acquired ({}).", resource);
    }

    void ensureRequiredZnodesExist(ZooKeeper zookeeper, String znode) throws KeeperException,
                                                                      InterruptedException {
        ZooKeeperHelper.mkdirp(zookeeper, znode);
        ZooKeeperHelper.createIfNotThere(zookeeper, QUEUE_NODE);
        ZooKeeperHelper.createIfNotThere(zookeeper, POOL_NODE);
    }

    public static ResourceClaim claim(ZooKeeperConnection zooKeeperConnection, int poolSize,
                                      String znode) throws IOException {
        return new ResourceClaim(zooKeeperConnection, poolSize, znode);
    }

    public int get() {
        if (state != State.HAS_CLAIM) {
            throw new IllegalStateException("Resource claim not held.");
        }
        return resource;
    }

    public void close() {
        close(false);
    }

    public void close(boolean nodeAlreadyDeleted) {
        if (state == State.CLAIM_RELINQUISHED) {
            return;
        }
        state = State.CLAIM_RELINQUISHED;
        zooKeeperConnection.deregisterObserver(this);

        logger.debug("Closing resource-claim ({}).", resource);

        if (nodeAlreadyDeleted)
            return;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                relinquishResource(zookeeper, POOL_NODE, resource);
            }
        }, TimeUnit.SECONDS.toMillis(2));
    }

    static String acquireLock(ZooKeeper zookeeper, String lockNode) throws KeeperException,
                                                                    InterruptedException {
        String placeInLine = takeQueueTicket(zookeeper, lockNode);
        logger.debug("Acquiring lock, waiting in queue: {}.", placeInLine);

        return waitInLine(zookeeper, lockNode, placeInLine);
    }

    static String takeQueueTicket(ZooKeeper zookeeper, String lockNode) throws InterruptedException,
                                                                        KeeperException {
        String ticket = String.format("nr-%014d-%04d", System.currentTimeMillis(),
            random.nextInt(10000));
        if (grabTicket(zookeeper, lockNode, ticket)) {
            return ticket;
        } else {
            return takeQueueTicket(zookeeper, lockNode);
        }
    }

    static void releaseTicket(ZooKeeper zookeeper, String lockNode,
                              String ticket) throws KeeperException, InterruptedException {

        logger.debug("Releasing ticket {}.", ticket);
        try {
            zookeeper.delete(lockNode + "/" + ticket, -1);
        } catch (KeeperException e) {
            if (e.code() != KeeperException.Code.NONODE) {
                // If it the node is already gone, than that is fine, otherwise:
                throw e;
            }
        }
    }

    static String waitInLine(ZooKeeper zookeeper, String lockNode,
                             String placeInLine) throws KeeperException, InterruptedException {

        List<String> children = zookeeper.getChildren(lockNode, false);

        Collections.sort(children);

        if (children.size() == 0) {
            logger.warn("getChildren() returned empty list, but we created a ticket.");
            return acquireLock(zookeeper, lockNode);
        }

        boolean lockingTicketExists = children.get(0).equals(LOCKING_TICKET);
        if (lockingTicketExists) {
            children.remove(0);
        }

        int positionInQueue = -1;
        int i = 0;
        for (String child : children) {
            if (child.equals(placeInLine)) {
                positionInQueue = i;
                break;
            }
            i++;
        }

        if (positionInQueue < 0) {
            throw new RuntimeException(
                "Created node (" + placeInLine + ") not found in getChildren().");
        }

        String placeBeforeUs;
        if (positionInQueue == 0) {
            if (grabTicket(zookeeper, lockNode, LOCKING_TICKET)) {
                releaseTicket(zookeeper, lockNode, placeInLine);
                return LOCKING_TICKET;
            } else {
                placeBeforeUs = LOCKING_TICKET;
            }
        } else {
            placeBeforeUs = children.get(positionInQueue - 1);
        }

        final CountDownLatch latch = new CountDownLatch(1);
        Stat stat = zookeeper.exists(lockNode + "/" + placeBeforeUs, event -> {
            latch.countDown();
        });

        if (stat != null) {
            logger.debug("Watching place in queue before us ({})", placeBeforeUs);
            latch.await();
        }

        return waitInLine(zookeeper, lockNode, placeInLine);
    }

    static boolean grabTicket(ZooKeeper zookeeper, String lockNode,
                              String ticket) throws InterruptedException, KeeperException {
        try {
            zookeeper.create(lockNode + "/" + ticket, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            if (e.code() == KeeperException.Code.NODEEXISTS) {
                logger.debug("Failed to claim ticket {}.", ticket);
                return false;
            } else {
                throw e;
            }
        }
        logger.debug("Claimed ticket {}.", ticket);
        return true;
    }

    int claimResource(ZooKeeper zookeeper, String poolNode, int poolSize) throws KeeperException,
                                                                          InterruptedException {

        logger.debug("Trying to claim a resource.");
        List<String> claimedResources = zookeeper.getChildren(poolNode, false);
        if (claimedResources.size() >= poolSize) {
            logger.debug("No resources available at the moment (pool size: {}), waiting.",
                poolSize);
            final CountDownLatch latch = new CountDownLatch(1);
            zookeeper.getChildren(poolNode, event -> latch.countDown());
            latch.await();
            return claimResource(zookeeper, poolNode, poolSize);
        }

        for (int i = 0; i < poolSize; i++) {
            String resourcePath = Integer.toString(i);
            if (!claimedResources.contains(resourcePath)) {
                String node;
                try {
                    logger.debug("Trying to claim seemingly available resource {}.", resourcePath);
                    node = zookeeper.create(poolNode + "/" + resourcePath, new byte[0],
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (KeeperException e) {
                    if (e.code() == KeeperException.Code.NODEEXISTS) {
                        continue;
                    } else {
                        throw e;
                    }
                }

                zookeeper.exists(node, event -> {
                    if (event.getType() == EventType.NodeDeleted) {
                        logger.debug("Resource-claim node unexpectedly deleted ({})", resource);
                        close(true);
                    }
                });

                logger.debug("Successfully claimed resource {}.", resourcePath);
                return i;
            }
        }

        return claimResource(zookeeper, poolNode, poolSize);
    }

    private void relinquishResource(ZooKeeper zookeeper, String poolNode, int resource) {
        logger.debug("Relinquishing claimed resource {}.", resource);
        try {
            zookeeper.delete(poolNode + "/" + Integer.toString(resource), -1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (KeeperException e) {
            logger.error("Failed to remove resource claim node {}/{}", poolNode, resource);
        }
    }

    public String getConfiguredZNode() {
        return ZNODE;
    }

    @Override
    public void disconnected() {
        logger.debug(
            "Disconnected from ZooKeeper quorum, this invalidates the claim to resource {}.",
            resource);
        state = State.CLAIM_RELINQUISHED;
        zooKeeperConnection.deregisterObserver(this);
    }

    @Override
    public void connected() {
        // No-op.
    }

    public enum State {
                       UNCLAIMED, HAS_CLAIM, CLAIM_RELINQUISHED
    }
}
