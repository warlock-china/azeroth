package cn.com.warlock.emitter.bytes;

import static cn.com.warlock.emitter.ParameterUtil.assertParameterWithinBounds;

/**
 * 火箭发射标准台，定义发射种类
 */
public class Blueprint {
    /**
     * 最大时间戳在2109年
     */
    public final static long MAX_TIMESTAMP        = 0x3FFFFFFFFFFL;

    /**
     * 使用时间戳补码限制64个
     */
    public final static int  MAX_SEQUENCE_COUNTER = 63;

    /**
     * 最大发射器站台
     */
    public final static int  MAX_GENERATOR_ID     = 255;

    /**
     * 最大集群节点
     */
    public final static int  MAX_CLUSTER_ID       = 15;

    final long               timestamp;
    final int                sequence;
    final int                generatorId;
    final int                clusterId;

    /**
     * @param timestamp   Milliseconds since the Unix epoch.
     * @param sequence    Sequence counter.
     * @param generatorId Generator ID.
     * @param clusterId   Cluster ID.
     * @see #MAX_CLUSTER_ID
     * @see #MAX_GENERATOR_ID
     */
    public Blueprint(long timestamp, int sequence, int generatorId, int clusterId) {
        assertParameterWithinBounds("timestamp", 0, MAX_TIMESTAMP, timestamp);
        assertParameterWithinBounds("sequence counter", 0, MAX_SEQUENCE_COUNTER, sequence);
        assertParameterWithinBounds("generator-ID", 0, MAX_GENERATOR_ID, generatorId);
        assertParameterWithinBounds("cluster-ID", 0, MAX_CLUSTER_ID, clusterId);

        this.timestamp = timestamp;
        this.sequence = sequence;
        this.generatorId = generatorId;
        this.clusterId = clusterId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getSequence() {
        return sequence;
    }

    public int getGeneratorId() {
        return generatorId;
    }

    public int getClusterId() {
        return clusterId;
    }

    @Override
    public String toString() {
        return String.format(
            "{\n  timestamp: %d,\n  sequence: %d,\n  generator: %d,\n  cluster: %d\n}", timestamp,
            sequence, generatorId, clusterId);
    }
}
