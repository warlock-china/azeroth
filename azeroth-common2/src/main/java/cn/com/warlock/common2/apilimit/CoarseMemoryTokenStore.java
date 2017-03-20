package cn.com.warlock.common2.apilimit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseMemoryTokenStore implements TokenStore {

    private final Map<Key, StoreEntry> cache;
    
    private final Lock lock;

    public CoarseMemoryTokenStore() {
        this.cache = new HashMap<Key, StoreEntry>();
        this.lock = new ReentrantLock();
    }
    
    public StoreEntry create(Key key, int timeToLiveInSecs) {
        try {
            StoreEntryImpl result = new StoreEntryImpl(timeToLiveInSecs);
            cache.put(key, result);
            return result;
        } finally {
            lock.unlock();
        }
    }

    public StoreEntry get(Key key) {
        lock.lock();
        
        StoreEntry result = cache.get(key);
        
        if (!(result == null || result.isExpired())) {
            
            lock.unlock();
            return result;
        }  else {
            
            result = null;
            cache.put(key, result);
        }
        
        return result;
    }

}
