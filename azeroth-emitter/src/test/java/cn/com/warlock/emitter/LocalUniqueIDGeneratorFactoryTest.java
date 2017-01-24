package cn.com.warlock.emitter;

import org.junit.Test;

import cn.com.warlock.emitter.LocalUniqueIDGeneratorFactory;

public class LocalUniqueIDGeneratorFactoryTest {

    @Test(expected = IllegalArgumentException.class)
    public void outOfBoundsGeneratorIDTest() {
        LocalUniqueIDGeneratorFactory.generatorFor(256, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void outOfBoundsClusterIDTest() {
        LocalUniqueIDGeneratorFactory.generatorFor(0, 16);
    }

    @Test(expected = IllegalArgumentException.class)
    public void outOfBoundsGeneratorIDNegativeTest() {
        LocalUniqueIDGeneratorFactory.generatorFor(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void outOfBoundsClusterIDNegativeTest() {
        LocalUniqueIDGeneratorFactory.generatorFor(0, -1);
    }
}