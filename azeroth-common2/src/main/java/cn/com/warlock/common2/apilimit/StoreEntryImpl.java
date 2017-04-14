package cn.com.warlock.common2.apilimit;

import java.util.concurrent.atomic.AtomicInteger;

class StoreEntryImpl implements StoreEntry {

    private final long          expiry;

    private final AtomicInteger counter;

    StoreEntryImpl(int timeToLive) {
        this.expiry = System.currentTimeMillis() + timeToLive * 1000;
        this.counter = new AtomicInteger(0);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiry;
    }

    public int incrementAndGet() {
        return this.counter.incrementAndGet();
    }

}
