package cn.com.warlock.common2.apilimit;

public interface StoreEntry {

    int incrementAndGet();

    boolean isExpired();
}