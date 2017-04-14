package cn.com.warlock.common2.apilimit.circuitbreaker;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.com.warlock.common2.apilimit.circuitbreaker.CircuitBreaker;
import cn.com.warlock.common2.apilimit.circuitbreaker.CircuitBreakerConfiguration;

public class TestCircuitBreaker {

    @Test
    public void basicUsage() throws Exception {
        CircuitBreakerConfiguration config = new CircuitBreakerConfiguration();
        config.setThreshold(1);
        config.setTimeoutInMillis(100);
        CircuitBreaker circuitBreaker = config.createCircuitBreaker();

        assertEquals(0L, circuitBreaker.getTripCount());
        assertEquals("CLOSED", circuitBreaker.getCurrentState());

        Runnable failingOperation = new Runnable() {

            public void run() {
                doFailingOp();
            }
        };

        tryGuardedOperation(circuitBreaker, failingOperation);

        assertEquals(0L, circuitBreaker.getTripCount());
        assertEquals("CLOSED", circuitBreaker.getCurrentState());

        tryGuardedOperation(circuitBreaker, failingOperation);

        assertEquals("第二次模拟失败之后跳闸", 1L, circuitBreaker.getTripCount());
        assertEquals("OPEN", circuitBreaker.getCurrentState());

    }

    @Test
    public void willAttemptReset() throws Exception {
        CircuitBreakerConfiguration config = new CircuitBreakerConfiguration();
        config.setThreshold(1);
        config.setTimeoutInMillis(100);
        CircuitBreaker circuitBreaker = config.createCircuitBreaker();

        assertEquals(0L, circuitBreaker.getTripCount());
        assertEquals("CLOSED", circuitBreaker.getCurrentState());

        Runnable failingOperation = new Runnable() {

            public void run() {
                doFailingOp();
            }
        };

        tryGuardedOperation(circuitBreaker, failingOperation);

        assertEquals(0L, circuitBreaker.getTripCount());
        assertEquals("CLOSED", circuitBreaker.getCurrentState());

        tryGuardedOperation(circuitBreaker, failingOperation);

        assertEquals("第二次模拟失败之后跳闸", 1L, circuitBreaker.getTripCount());
        assertEquals("OPEN", circuitBreaker.getCurrentState());

        Thread.sleep(101);

        tryGuardedOperation(circuitBreaker, new Runnable() {

            public void run() {
                doSuccessOp();
            }
        });

        assertEquals("当模拟一次成功的操作后，再次变成关闭状态了", "CLOSED", circuitBreaker.getCurrentState());
    }

    @Test
    public void canBeResetAndSuccessfullyCarryOn() throws Exception {
        CircuitBreakerConfiguration config = new CircuitBreakerConfiguration();
        config.setThreshold(0);
        config.setTimeoutInMillis(100);
        CircuitBreaker circuitBreaker = config.createCircuitBreaker();

        assertEquals(0L, circuitBreaker.getTripCount());
        assertEquals("CLOSED", circuitBreaker.getCurrentState());

        Runnable failingOperation = new Runnable() {

            public void run() {
                doFailingOp();
            }
        };

        tryGuardedOperation(circuitBreaker, failingOperation);

        assertEquals("第二次模拟失败之后跳闸", 1L, circuitBreaker.getTripCount());
        assertEquals("OPEN", circuitBreaker.getCurrentState());

        circuitBreaker.reset();

        assertEquals("CLOSED", circuitBreaker.getCurrentState());

        tryGuardedOperation(circuitBreaker, new Runnable() {

            public void run() {
                doSuccessOp();
            }
        });

        assertEquals("当模拟一次成功的操作后，再次变成关闭状态了,这次不用sleep了发现没？", "CLOSED",
            circuitBreaker.getCurrentState());

    }

    private void tryGuardedOperation(CircuitBreaker circuitBreaker, Runnable operation) {
        try {
            circuitBreaker.before();
            operation.run();
            circuitBreaker.after();
        } catch (Throwable e) {
            circuitBreaker.handleFailure();
        }
    }

    private void doFailingOp() {
        throw new RuntimeException("假装失败了");
    }

    private void doSuccessOp() {
        //System.out.println("模拟成功的操作");
    }
}
