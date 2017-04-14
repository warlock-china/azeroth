package cn.com.warlock.emitter;

import static cn.com.warlock.emitter.ParameterUtil.assertParameterWithinBounds;

import java.io.IOException;

import cn.com.warlock.emitter.bytes.Blueprint;

public class LocalGeneratorIdentity implements GeneratorIdentityHolder {
    private final int clusterId;
    private final int generatorId;
    private boolean   closed = false;

    LocalGeneratorIdentity(int clusterId, int generatorId) {
        this.clusterId = clusterId;
        this.generatorId = generatorId;
    }

    public static LocalGeneratorIdentity with(int clusterId, int generatorId) {
        assertParameterWithinBounds("generatorId", 0, Blueprint.MAX_GENERATOR_ID, generatorId);
        assertParameterWithinBounds("clusterId", 0, Blueprint.MAX_CLUSTER_ID, clusterId);
        return new LocalGeneratorIdentity(clusterId, generatorId);
    }

    @Override
    public int getClusterId() {
        if (closed)
            throw new IllegalStateException("Resource was closed.");
        return clusterId;
    }

    @Override
    public int getGeneratorId() {
        if (closed)
            throw new IllegalStateException("Resource was closed.");
        return generatorId;
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }
}
