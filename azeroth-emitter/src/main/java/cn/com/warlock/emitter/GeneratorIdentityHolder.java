package cn.com.warlock.emitter;

import java.io.Closeable;

public interface GeneratorIdentityHolder extends Closeable {
    int getClusterId() throws GeneratorException;
    int getGeneratorId() throws GeneratorException;
}
