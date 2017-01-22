package cn.com.warlock.kafka.serializer;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import cn.com.warlock.common.serializer.SerializeUtils;

public class KyroMessageDeserializer implements Deserializer<Object> {
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public Object deserialize(String topic, byte[] data) {
        if (data == null)
            return null;
        else
            return SerializeUtils.deserialize(data);
    }

    @Override
    public void close() {
        // nothing to do
    }
}
