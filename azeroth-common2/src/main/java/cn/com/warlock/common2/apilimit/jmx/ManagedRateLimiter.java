package cn.com.warlock.common2.apilimit.jmx;

import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.monitor.MonitorNotification;

import cn.com.warlock.common2.apilimit.Key;
import cn.com.warlock.common2.apilimit.RateLimiter;
import cn.com.warlock.common2.apilimit.Token;

public class ManagedRateLimiter extends NotificationBroadcasterSupport
                                implements ManagedRateLimiterMBean {

    private static final String JMX_MONITOR_RATE_LIMIT_SERVICE_TYPE = "jmx.monitor.rate-limit.service";

    private final RateLimiter   delegate;

    private long                sequenceNumber;

    public ManagedRateLimiter(RateLimiter delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("delegate cannot be null");
        }
        this.delegate = delegate;
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = new String[] { JMX_MONITOR_RATE_LIMIT_SERVICE_TYPE,
                                        MonitorNotification.THRESHOLD_VALUE_EXCEEDED };
        MBeanNotificationInfo info = new MBeanNotificationInfo(types, Notification.class.getName(),
            "rate-limited request processed");
        return new MBeanNotificationInfo[] { info };
    }

    public Token getToken(Key key) {
        Token token = delegate.getToken(key);

        if (token.isUsable()) {
            sendNotification(new Notification(JMX_MONITOR_RATE_LIMIT_SERVICE_TYPE, this,
                getSequenceNumber(), "allowed request " + key));
        } else {
            sendNotification(new Notification(MonitorNotification.THRESHOLD_VALUE_EXCEEDED, this,
                getSequenceNumber(), "denied request " + key));
        }

        return token;
    }

    public boolean isEnabled() {
        return this.delegate.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        this.delegate.setEnabled(enabled);
    }

    public int getAllowedRequests() {
        return delegate.getAllowedRequests();
    }

    public int getDuration() {
        return delegate.getDuration();
    }

    public void setAllowedRequests(int allowedRequests) {
        delegate.setAllowedRequests(allowedRequests);
    }

    public void setDuration(int durationInSeconds) {
        delegate.setDuration(durationInSeconds);
    }

    private synchronized long getSequenceNumber() {
        return ++this.sequenceNumber;
    }

}
