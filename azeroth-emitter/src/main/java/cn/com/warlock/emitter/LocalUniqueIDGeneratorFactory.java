package cn.com.warlock.emitter;

import static cn.com.warlock.emitter.ParameterUtil.assertParameterWithinBounds;

import java.util.HashMap;
import java.util.Map;

import cn.com.warlock.emitter.bytes.Blueprint;

public class LocalUniqueIDGeneratorFactory {
    final static Map<String, IDGenerator> instances = new HashMap<>();

    public synchronized static IDGenerator generatorFor(int generatorId, int clusterId) {
        assertParameterWithinBounds("generatorId", 0, Blueprint.MAX_GENERATOR_ID, generatorId);
        assertParameterWithinBounds("clusterId", 0, Blueprint.MAX_CLUSTER_ID, clusterId);
        String generatorAndCluster = String.format("%d_%d", generatorId, clusterId);
        if (!instances.containsKey(generatorAndCluster)) {
            GeneratorIdentityHolder identityHolder = LocalGeneratorIdentity.with(clusterId,
                generatorId);
            instances.putIfAbsent(generatorAndCluster, new BaseUniqueIDGenerator(identityHolder));
        }
        return instances.get(generatorAndCluster);
    }
}
