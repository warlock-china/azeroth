package cn.com.warlock.emitter;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.NoSuchElementException;

public class AutoRefillStack implements IDGenerator {

    static final int    DEFAULT_BATCH_SIZE = 500;

    final int           batchSize;
    final IDGenerator   generator;
    final Deque<byte[]> idStack            = new ArrayDeque<>();

    protected AutoRefillStack(IDGenerator generator, int batchSize) {
        this.batchSize = batchSize;
        this.generator = generator;
    }

    public static IDGenerator decorate(IDGenerator generator) {
        return new AutoRefillStack(generator, DEFAULT_BATCH_SIZE);
    }

    public static IDGenerator decorate(IDGenerator generator, int batchSize) {
        return new AutoRefillStack(generator, batchSize);
    }

    @PreDestroy
    @Override
    public void close() throws IOException {
        generator.close();
    }

    @Override
    public synchronized byte[] generate() throws GeneratorException {
        return popOne();
    }

    @Override
    public synchronized Deque<byte[]> batch(int size) throws GeneratorException {
        if (size < 0) {
            size = 0;
        }
        Deque<byte[]> batch = new ArrayDeque<>(size);
        while (size > 0) {
            batch.add(popOne());
            size--;
        }
        return batch;
    }

    byte[] popOne() throws GeneratorException {
        try {
            return idStack.pop();
        } catch (NoSuchElementException e) {
            // Cached stack is empty, load up a fresh stack.
            idStack.addAll(generator.batch(batchSize));
            return popOne();
        }
    }
}
