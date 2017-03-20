package cn.com.warlock.common2.apilimit;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.BlockingCache;
import net.sf.ehcache.constructs.blocking.LockTimeoutException;

public class EhcacheTokenStore implements TokenStore {

    private BlockingCache cache;

    public void setCache(Ehcache cache) {
        BlockingCache ref;

        if (!(cache instanceof BlockingCache)) {
            ref = new BlockingCache(cache);
            cache.getCacheManager().replaceCacheWithDecoratedCache(cache, new BlockingCache(cache));
        } else {
            ref = (BlockingCache) cache;
        }

        this.cache = ref;
    }

    public StoreEntry create(Key key, int timeToLive) {
        StoreEntryImpl result = new StoreEntryImpl(timeToLive);
        Element element = new Element(key, result);
        element.setTimeToLive(timeToLive);
        cache.put(element);
        return result;
    }

    public StoreEntry get(Key key) {

        Element entry = null;

        try {

            entry = cache.get(key);
        } catch (LockTimeoutException e) {
            throw new RuntimeException();
        } catch (RuntimeException e) {

            cache.put(new Element(key, null));
        }

        StoreEntry result = null;

        if (entry != null) {

            result = (StoreEntry) entry.getObjectValue();
        }

        return result;
    }

}
