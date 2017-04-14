package cn.com.warlock.common2.apilimit;

public class FixedBucket implements RateLimiter {

    private volatile boolean enabled         = true;

    private int              timeToLive      = 1;

    private int              allowedRequests = 1;

    private TokenStore       cache;

    public void setTokenStore(TokenStore cache) {
        this.cache = cache;
    }

    public int getAllowedRequests() {
        return this.allowedRequests;
    }

    public void setAllowedRequests(int allowedRequests) {
        if (allowedRequests > 0) {
            this.allowedRequests = allowedRequests;
        }
    }

    public Token getToken(Key key) {
        Token result = TokenInstance.UNUSABLE;

        if (!enabled) {
            result = TokenInstance.USABLE;
        } else {

            StoreEntry entry = cache.get(key);

            if (entry == null) {

                entry = cache.create(key, timeToLive);
            }

            int current = entry.incrementAndGet();

            if (current <= allowedRequests) {
                result = TokenInstance.USABLE;
            }
        }

        return result;
    }

    public void init() {

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setDuration(int durationInSeconds) {
        if (durationInSeconds > 0) {
            this.timeToLive = durationInSeconds;
        }
    }

    public int getDuration() {
        return this.timeToLive;
    }

}
