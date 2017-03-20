package cn.com.warlock.common2.apilimit.circuitbreaker;

interface CircuitBreakerState {

    void after(CircuitBreaker circuitBreaker);

    void before(CircuitBreaker circuitBreaker) throws CircuitBreakerException;

    void handleFailure(CircuitBreaker circuitBreaker);

    long getTimeToReset();

}
