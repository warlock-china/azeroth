package cn.com.warlock.kafka.serializer;

import org.apache.kafka.common.serialization.Deserializer;

import cn.com.warlock.common.serializer.SerializeUtils;
import kafka.serializer.Decoder;

public class MessageDecoder implements Decoder<Object> {

    private Deserializer<Object> deserializer;

    public MessageDecoder() {
    }

    public MessageDecoder(Deserializer<Object> deserializer) {
        super();
        this.deserializer = deserializer;
    }

    @Override
    public Object fromBytes(byte[] bytes) {
        if (deserializer != null)
            return deserializer.deserialize(null, bytes);
        return SerializeUtils.deserialize(bytes);
    }
}
