package cn.com.warlock.common2.apilimit;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;

import org.junit.After;
import org.junit.Before;

import cn.com.warlock.common2.apilimit.EhcacheTokenStore;
import cn.com.warlock.common2.apilimit.TokenStore;

public class TestFixedBucketWithEhcache extends FixedBucketTests {

    private CacheManager cacheManager;

    private Ehcache      cache;

    @Before
    public void setup() {
        this.cache = new Cache("test-token-store", 100, false, false, 100, 10);
        this.cacheManager = CacheManager.create();
        this.cacheManager.addCache(this.cache);
    }

    @After
    public void teardown() {
        cacheManager.shutdown();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TokenStore createTokenStore() {
        EhcacheTokenStore tokenStore = new EhcacheTokenStore();
        tokenStore.setCache(this.cache);
        return tokenStore;
    }

}
