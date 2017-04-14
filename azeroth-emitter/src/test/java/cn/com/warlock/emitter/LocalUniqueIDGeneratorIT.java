package cn.com.warlock.emitter;

import org.junit.Test;

import cn.com.warlock.emitter.IDGenerator;
import cn.com.warlock.emitter.LocalUniqueIDGeneratorFactory;
import cn.com.warlock.emitter.bytes.Blueprint;
import cn.com.warlock.emitter.bytes.IDBuilder;

import java.util.Deque;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LocalUniqueIDGeneratorIT {

    @Test
    public void batchTest() throws Exception {
        final int GENERATOR_ID = 42;
        final int CLUSTER_ID = 7;
        final int BATCH_SIZE = 500;
        IDGenerator generator = LocalUniqueIDGeneratorFactory.generatorFor(GENERATOR_ID,
            CLUSTER_ID);

        Deque<byte[]> stack = generator.batch(BATCH_SIZE);
        assertThat(stack.size(), is(BATCH_SIZE));

        Blueprint blueprint = IDBuilder.parse(stack.pop());
        assertThat(blueprint.getGeneratorId(), is(GENERATOR_ID));
        assertThat(blueprint.getClusterId(), is(CLUSTER_ID));
    }

    @Test
    public void highGeneratorIdTest() throws Exception {
        final int GENERATOR_ID = 255;
        final int CLUSTER_ID = 15;
        IDGenerator generator = LocalUniqueIDGeneratorFactory.generatorFor(GENERATOR_ID,
            CLUSTER_ID);

        byte[] id = generator.generate();

        Blueprint blueprint = IDBuilder.parse(id);
        assertThat(blueprint.getGeneratorId(), is(GENERATOR_ID));
        assertThat(blueprint.getClusterId(), is(CLUSTER_ID));
    }
}