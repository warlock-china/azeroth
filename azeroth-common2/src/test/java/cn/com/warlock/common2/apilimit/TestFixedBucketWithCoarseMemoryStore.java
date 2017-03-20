package cn.com.warlock.common2.apilimit;

import cn.com.warlock.common2.apilimit.CoarseMemoryTokenStore;
import cn.com.warlock.common2.apilimit.TokenStore;

public class TestFixedBucketWithCoarseMemoryStore extends FixedBucketTests {

    @Override
    protected TokenStore createTokenStore() {
        return new CoarseMemoryTokenStore();
    }

}
