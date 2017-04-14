package cn.com.warlock.emitter.zk;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.warlock.emitter.BaseUniqueIDGenerator;
import cn.com.warlock.emitter.IDGenerator;
import cn.com.warlock.emitter.zk.connection.ZooKeeperConnection;

public class SynchronizedUniqueIDGeneratorFactory {
    final static Logger                   logger    = LoggerFactory
        .getLogger(SynchronizedUniqueIDGeneratorFactory.class);
    final static Map<String, IDGenerator> instances = new HashMap<>();

    public static synchronized IDGenerator generatorFor(ZooKeeperConnection zooKeeperConnection,
                                                        String znode) throws IOException {

        if (!instances.containsKey(znode)) {
            final int clusterId = ClusterID.get(zooKeeperConnection.get(), znode);
            SynchronizedGeneratorIdentity generatorIdentityHolder = new SynchronizedGeneratorIdentity(
                zooKeeperConnection, znode, clusterId, null);

            return generatorFor(generatorIdentityHolder);
        }
        return instances.get(znode);
    }

    public static synchronized IDGenerator generatorFor(SynchronizedGeneratorIdentity synchronizedGeneratorIdentity) throws IOException {

        String instanceKey = synchronizedGeneratorIdentity.getZNode();
        if (!instances.containsKey(instanceKey)) {
            logger.debug("Creating new instance.");
            instances.putIfAbsent(instanceKey,
                new BaseUniqueIDGenerator(synchronizedGeneratorIdentity));
        }
        return instances.get(instanceKey);
    }
}
