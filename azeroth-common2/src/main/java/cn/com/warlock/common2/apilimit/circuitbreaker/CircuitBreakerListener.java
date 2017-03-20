package cn.com.warlock.common2.apilimit.circuitbreaker;

public interface CircuitBreakerListener {

    void attemptReset();

    void reset();

    void tripped();
}
