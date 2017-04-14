package cn.com.warlock.emitter.bytes;

import org.junit.Test;

import cn.com.warlock.emitter.bytes.Blueprint;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class BlueprintTest {
    @Test
    public void toStringTest() {
        Blueprint blueprint = new Blueprint(System.currentTimeMillis(), 0, 0, 0);
        assertThat(blueprint.toString(), is(notNullValue()));
        assertThat(blueprint.toString().length(), is(not(0)));
    }
}