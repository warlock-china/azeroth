package cn.com.warlock.common2.apilimit.circuitbreaker;

public class CircuitBreakerException extends Exception {

    private static final long serialVersionUID = -2000914426184917307L;

    public CircuitBreakerException() {
        super();
    }

    public CircuitBreakerException(String message) {
        super(message);
    }

    public CircuitBreakerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CircuitBreakerException(Throwable cause) {
        super(cause);
    }
}
