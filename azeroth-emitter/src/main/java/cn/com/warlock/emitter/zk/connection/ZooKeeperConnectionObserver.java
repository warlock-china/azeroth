package cn.com.warlock.emitter.zk.connection;

public interface ZooKeeperConnectionObserver {
    void disconnected();

    void connected();
}
