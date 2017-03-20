package cn.com.warlock.common2.apilimit;

import cn.com.warlock.common2.apilimit.Key;

class RateLimiterKey implements Key {

    @Override
    public String toString() {
        return "warlock-test-key-" + System.identityHashCode(this);
    }

}