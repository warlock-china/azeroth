package cn.com.warlock.emitter.bytes;

import static cn.com.warlock.emitter.ParameterUtil.assertNotNullEightBytes;

import java.nio.ByteBuffer;

/**
 * ClassName: IDBuilder <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:  8字节id生成器，思路来源于Lable <br/>
 * <pre>TTTTTTTT TTTTTTTT TTTTTTTT TTTTTTTT TTTTTTTT TTSSSSSS ....GGGG GGGGCCCC</pre>
 * date: Jan 24, 2017 4:01:40 PM <br/>
 *
 * @author warlock
 * @version 
 * @since JDK 1.8
 */
public class IDBuilder {
    public static byte[] build(Blueprint blueprint) {
        long reverseTimestamp = Long.reverse(blueprint.getTimestamp());
        ByteBuffer bb = ByteBuffer.allocate(8);
        byte[] tsBytes = bb.putLong(reverseTimestamp).array();

        int or = tsBytes[5] | (byte) blueprint.getSequence();
        tsBytes[5] = (byte) or;

        int generatorAndCluster = blueprint.getGeneratorId() << 4;
        generatorAndCluster += blueprint.getClusterId();

        tsBytes[7] = (byte) generatorAndCluster;
        generatorAndCluster >>>= 8;
        tsBytes[6] = (byte) generatorAndCluster;

        return tsBytes;
    }

    public static Blueprint parse(byte[] id) {
        assertNotNullEightBytes(id);

        int sequence = parseSequenceIdNoChecks(id);
        int generatorId = parseGeneratorIdNoChecks(id);
        int clusterId = parseClusterIdNoChecks(id);
        long timestamp = parseTimestampNoChecks(id);

        return new Blueprint(timestamp, sequence, generatorId, clusterId);
    }

    public static int parseSequenceId(byte[] id) {
        assertNotNullEightBytes(id);
        return parseSequenceIdNoChecks(id);
    }

    public static int parseGeneratorId(byte[] id) {
        assertNotNullEightBytes(id);
        return parseGeneratorIdNoChecks(id);
    }

    public static int parseClusterId(byte[] id) {
        assertNotNullEightBytes(id);
        return parseClusterIdNoChecks(id);
    }

    public static long parseTimestamp(byte[] id) {
        assertNotNullEightBytes(id);
        return parseTimestampNoChecks(id);
    }

    private static int parseSequenceIdNoChecks(byte[] id) {
        return id[5] & 0x3F;
    }

    private static int parseGeneratorIdNoChecks(byte[] id) {
        return (id[7] >> 4 & 0x0F) | (id[6] << 4);
    }

    private static int parseClusterIdNoChecks(byte[] id) {
        return id[7] & 0x0F;
    }

    private static long parseTimestampNoChecks(byte[] id) {
        byte[] copy = id.clone();

        copy[5] = (byte) (copy[5] & 0xC0);
        copy[6] = 0;
        copy[7] = 0;

        ByteBuffer bb = ByteBuffer.wrap(copy);
        return Long.reverse(bb.getLong());
    }
}
