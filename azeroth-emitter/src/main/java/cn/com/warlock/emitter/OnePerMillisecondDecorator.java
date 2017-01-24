package cn.com.warlock.emitter;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

public class OnePerMillisecondDecorator implements IDGenerator {
    final IDGenerator generator;
    long previousInvocation = 0;
    byte[] previous = null;

    protected OnePerMillisecondDecorator(IDGenerator generator) {
        this.generator = generator;
    }

    public static IDGenerator decorate(IDGenerator generator) {
        return new OnePerMillisecondDecorator(generator);
    }

    @Override
    public void close() throws IOException {
        generator.close();
    }

    @Override
    public byte[] generate() throws GeneratorException {
        long now = System.currentTimeMillis();
        while (previousInvocation == now) {
            sleepAMillisecond();
            now = System.currentTimeMillis();
        }
        previousInvocation = now;

        byte[] id = generator.generate();
        if (previous != null) {
            while (previous[0] == id[0]) {
                sleepAMillisecond();
                id = generator.generate();
            }
        }

        previous = id;
        return id;
    }

    private void sleepAMillisecond() {
        try {
            TimeUnit.MILLISECONDS.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Deque<byte[]> batch(int size) throws GeneratorException {
        Deque<byte[]> deck = new ArrayDeque<>();
        for (int i = 0; i < size; i++) {
            deck.add(generate());
        }
        return deck;
    }
}
