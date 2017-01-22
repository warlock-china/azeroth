package cn.com.warlock.kafka.serializer;

import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

public class JdkMessageDeserializer implements Deserializer<Object> {
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public Object deserialize(String topic, byte[] data) {
        if (data == null)
            return null;
        else
            return SerializationUtils.deserialize(data);
    }

    @Override
    public void close() {
        // nothing to do
    }
}
