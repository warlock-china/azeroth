package cn.com.warlock.emitter;

import java.io.Closeable;
import java.util.Deque;

public interface IDGenerator extends Closeable {
    byte[] generate() throws GeneratorException;

    Deque<byte[]> batch(int size) throws GeneratorException;
}
