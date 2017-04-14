/**
 * 
 */
package cn.com.warlock.common2.lock;

public interface LockCaller<T> {

    /**
     * 持有锁的操作
     * @return
     */
    T onHolder();

    /**
     * 等待锁的操作
     * @return
     */
    T onWait();
}
