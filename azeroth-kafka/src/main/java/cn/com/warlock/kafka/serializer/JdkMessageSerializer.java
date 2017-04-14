package cn.com.warlock.kafka.serializer;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

public class JdkMessageSerializer implements Serializer<Serializable> {

    /**
     * Configure this class.
     *
     * @param configs configs in key/value pairs
     * @param isKey   whether is for key or value
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    /**
     * serialize
     *
     * @param topic topic associated with data
     * @param data  typed data
     * @return serialized bytes
     */
    @Override
    public byte[] serialize(String topic, Serializable data) {
        return SerializationUtils.serialize(data);
    }

    /**
     * Close this serializer
     */
    @Override
    public void close() {

    }
}
