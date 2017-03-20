package cn.com.warlock.common2.apilimit;

import cn.com.warlock.common2.apilimit.MemoryTokenStore;
import cn.com.warlock.common2.apilimit.TokenStore;

public class TestFixedBucketWithMemoryStore extends FixedBucketTests {

    @Override
    protected TokenStore createTokenStore() {
        return new MemoryTokenStore();
    }

}
