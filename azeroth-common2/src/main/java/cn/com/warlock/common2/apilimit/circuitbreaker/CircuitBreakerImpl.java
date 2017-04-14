package cn.com.warlock.common2.apilimit.circuitbreaker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class CircuitBreakerImpl implements CircuitBreaker {

    private final int                                  threshold;

    private final int                                  timeout;

    private final AtomicLong                           tripCount;

    private final AtomicReference<CircuitBreakerState> state;

    private final List<CircuitBreakerListener>         listeners;

    public CircuitBreakerImpl(int threshold, int timeout) {
        this.threshold = threshold;
        this.timeout = timeout;
        this.tripCount = new AtomicLong();
        this.listeners = new ArrayList<CircuitBreakerListener>();
        this.state = new AtomicReference<CircuitBreakerState>(new ClosedState(threshold));
    }

    public void addListener(CircuitBreakerListener listener) {
        this.listeners.add(listener);
    }

    public void after() {
        getState().after(this);
    }

    public void attemptReset() {
        setState(new HalfOpenState());

        notifyListeners(Notifications.ATTEMPT_RESET);
    }

    public void before() throws CircuitBreakerException {
        getState().before(this);
    }

    public void handleFailure() {
        getState().handleFailure(this);
    }

    public String getCurrentState() {
        return getState().toString();
    }

    public int getThreshold() {
        return this.threshold;
    }

    public long getTimeToResetInMillis() {
        return getState().getTimeToReset();
    }

    public long getTripCount() {
        return this.tripCount.get();
    }

    public void tripBreaker() {
        tripCount.incrementAndGet();
        setState(new OpenState(this.timeout));

        notifyListeners(Notifications.TRIPPED);
    }

    public void reset() {
        setState(new ClosedState(threshold));

        notifyListeners(Notifications.RESET);
    }

    private CircuitBreakerState getState() {
        return this.state.get();
    }

    private void notifyListeners(Notifications notifications) {
        for (CircuitBreakerListener listener : this.listeners) {
            try {
                notifications.notifyListener(listener);
            } catch (RuntimeException e) {
                /* ignore and carry on processing the others */
            }
        }
    }

    private void setState(CircuitBreakerState newState) {
        this.state.set(newState);
    }

    interface NotifyListener {

        void notifyListener(CircuitBreakerListener listener);
    }

    private static enum Notifications implements NotifyListener {

                                                                 ATTEMPT_RESET() {
                                                                     public void notifyListener(CircuitBreakerListener listener) {
                                                                         listener.attemptReset();
                                                                     }
                                                                 },

                                                                 RESET() {
                                                                     public void notifyListener(CircuitBreakerListener listener) {
                                                                         listener.reset();
                                                                     }
                                                                 },

                                                                 TRIPPED() {
                                                                     public void notifyListener(CircuitBreakerListener listener) {
                                                                         listener.tripped();
                                                                     }
                                                                 };

        public abstract void notifyListener(CircuitBreakerListener listener);

    }

}
