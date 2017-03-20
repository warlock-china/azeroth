package cn.com.warlock.common2.apilimit;

public interface TokenStore {

    StoreEntry get(Key key);

    StoreEntry create(Key key, int timeToLiveInSecs);

}
