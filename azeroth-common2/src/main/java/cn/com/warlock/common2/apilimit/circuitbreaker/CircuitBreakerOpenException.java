package cn.com.warlock.common2.apilimit.circuitbreaker;

public class CircuitBreakerOpenException extends CircuitBreakerException {
    
    private static final long serialVersionUID = -4097710178233282483L;

    public CircuitBreakerOpenException() {
        super();
    }

    public CircuitBreakerOpenException(String message) {
        super(message);
    }

    public CircuitBreakerOpenException(String message, Throwable cause) {
        super(message, cause);
    }

    public CircuitBreakerOpenException(Throwable cause) {
        super(cause);
    }
}
