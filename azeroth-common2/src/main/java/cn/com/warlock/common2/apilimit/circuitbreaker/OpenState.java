package cn.com.warlock.common2.apilimit.circuitbreaker;

public class OpenState implements CircuitBreakerState {

    private final long tripTime;

    private final int timeout;

    OpenState(int timeout) {
        this.tripTime = System.currentTimeMillis();
        this.timeout = timeout;
    }

    public void after(CircuitBreaker circuitBreakerImpl) {
        // no-op
    }

    public void before(CircuitBreaker circuitBreakerImpl) throws CircuitBreakerException {
        long now = System.currentTimeMillis();
        long elapsed = now - this.tripTime;

        if (elapsed > this.timeout) {
            circuitBreakerImpl.attemptReset();
        } else {
            throw new CircuitBreakerOpenException("保险丝没有合上哟.");
        }
    }

    public long getTimeToReset() {
        long now = System.currentTimeMillis();
        long elapsed = now - this.tripTime;

        if (elapsed < this.timeout) {

            return this.timeout - elapsed;
        }

        return 0;
    }

    public void handleFailure(CircuitBreaker circuitBreakerImpl) {
        // no-op
    }

    @Override
    public String toString() {
        return "OPEN";
    }

}
