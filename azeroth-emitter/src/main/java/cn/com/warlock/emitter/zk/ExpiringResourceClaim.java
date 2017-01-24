package cn.com.warlock.emitter.zk;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.warlock.emitter.zk.connection.ZooKeeperConnection;

public class ExpiringResourceClaim extends ResourceClaim {
    private static final Logger logger = LoggerFactory.getLogger(ExpiringResourceClaim.class);

    public final static long DEFAULT_TIMEOUT = TimeUnit.SECONDS.toMillis(30);

    ExpiringResourceClaim(ZooKeeperConnection zooKeeperConnection, int poolSize, String znode, long timeout) throws IOException {
        super(zooKeeperConnection, poolSize, znode);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                close();
            }
        }, timeout);
    }

    public static ResourceClaim claimExpiring(ZooKeeperConnection zooKeeperConnection, int poolSize, String znode)
            throws IOException {
        return claimExpiring(zooKeeperConnection, poolSize, znode, DEFAULT_TIMEOUT);
    }

    public static ResourceClaim claimExpiring(ZooKeeperConnection zooKeeperConnection,
                                              int poolSize,
                                              String znode,
                                              Long timeout)
            throws IOException {

        long timeoutNonNull = timeout == null ? DEFAULT_TIMEOUT : timeout;
        logger.debug("Preparing expiring resource-claim; will release it in {}ms.", timeout);

        return new ExpiringResourceClaim(zooKeeperConnection, poolSize, znode, timeoutNonNull);
    }
}
