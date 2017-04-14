package cn.com.warlock.emitter.zk;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ClusterID {
    final static String CLUSTER_ID_NODE    = "/cluster-id";
    final static int    DEFAULT_CLUSTER_ID = 0;

    public static int get(ZooKeeper zookeeper, String znode) throws IOException {
        try {
            Stat stat = zookeeper.exists(znode + CLUSTER_ID_NODE, false);
            if (stat == null) {
                ZooKeeperHelper.mkdirp(zookeeper, znode);
                ZooKeeperHelper.create(zookeeper, znode + CLUSTER_ID_NODE,
                    String.valueOf(DEFAULT_CLUSTER_ID).getBytes());
            }

            byte[] id = zookeeper.getData(znode + CLUSTER_ID_NODE, false, null);
            return Integer.valueOf(new String(id));
        } catch (KeeperException e) {
            throw new IOException(
                String.format("Failed to retrieve the cluster ID from the ZooKeeper quorum. "
                              + "Expected to find it at znode %s.",
                    znode + CLUSTER_ID_NODE),
                e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException(e);
        }
    }
}
