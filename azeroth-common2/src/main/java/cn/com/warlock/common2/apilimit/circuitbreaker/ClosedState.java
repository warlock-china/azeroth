package cn.com.warlock.common2.apilimit.circuitbreaker;

import java.util.concurrent.atomic.AtomicInteger;

class ClosedState implements CircuitBreakerState {

    private final AtomicInteger failureCount;

    private final int           threshold;

    ClosedState(int threshold) {
        this.threshold = threshold;
        this.failureCount = new AtomicInteger();
    }

    public void after(CircuitBreaker circuitBreakerImpl) {
        this.failureCount.set(0);
    }

    public void before(CircuitBreaker circuitBreakerImpl) {
        // no-op
    }

    public long getTimeToReset() {
        return -1;
    }

    public void handleFailure(CircuitBreaker circuitBreakerImpl) {
        int count = this.failureCount.incrementAndGet();

        if (count > threshold) {
            circuitBreakerImpl.tripBreaker();
        }
    }

    @Override
    public String toString() {
        return "CLOSED";
    }

}
