package cn.com.warlock.emitter;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

import cn.com.warlock.emitter.bytes.Blueprint;
import cn.com.warlock.emitter.bytes.IDBuilder;

public class BaseUniqueIDGenerator implements IDGenerator {
    protected final GeneratorIdentityHolder generatorIdentityHolder;

    long                                    previousTimestamp = 0;
    int                                     sequence          = 0;

    public BaseUniqueIDGenerator(GeneratorIdentityHolder generatorIdentityHolder) {
        this.generatorIdentityHolder = generatorIdentityHolder;
    }

    @Override
    public synchronized byte[] generate() throws GeneratorException {

        long now = System.currentTimeMillis();
        if (now == previousTimestamp) {
            sequence++;
        } else {
            sequence = 0;
        }
        if (sequence > Blueprint.MAX_SEQUENCE_COUNTER) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
                return generate();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        previousTimestamp = now;

        Blueprint blueprint = new Blueprint(now, sequence, generatorIdentityHolder.getGeneratorId(),
            generatorIdentityHolder.getClusterId());

        return IDBuilder.build(blueprint);
    }

    @Override
    public Deque<byte[]> batch(int size) throws GeneratorException {
        Deque<byte[]> stack = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
            stack.add(generate());
        }
        return stack;
    }

    @Override
    public void close() throws IOException {
        generatorIdentityHolder.close();
    }
}
