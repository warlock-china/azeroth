package cn.com.warlock.common2.apilimit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class MemoryTokenStore implements TokenStore {

    private final Cache<Key, StoreEntry> cache;

    private final Lock                   r;

    private final Lock                   w;

    public MemoryTokenStore() {
        this.cache = CacheBuilder.newBuilder().softValues().expireAfterAccess(120, TimeUnit.SECONDS)
            .build();
        ReadWriteLock lock = new ReentrantReadWriteLock();
        this.r = lock.readLock();
        this.w = lock.writeLock();
    }

    public StoreEntry get(Key key) {
        StoreEntry result;
        r.lock();
        try {
            result = this.cache.getIfPresent(key);
        } finally {
            r.unlock();
        }

        if (!(result == null || result.isExpired()))
            return result;

        w.lock();

        result = checkPopulateThisPeriod(key);

        return result;
    }

    public StoreEntry create(Key key, int timeToLive) {
        try {
            StoreEntryImpl entry = new StoreEntryImpl(timeToLive);
            cache.put(key, entry);
            return entry;
        } finally {
            w.unlock();
        }
    }

    private StoreEntry checkPopulateThisPeriod(Key key) {
        StoreEntry result = this.cache.getIfPresent(key);
        if (result == null) {
        } else if (result.isExpired()) {
            cache.invalidate(key);
            result = null;
        } else {
            w.unlock();
        }
        return result;
    }

}