package cn.com.warlock.common2.lock;

public class DistributeLockTemplate {

    private static final long _DEFAULT_LOCK_HOLD_MILLS = 30000;

    public static <T> T execute(String lockId, LockCaller<T> caller) {
        return execute(lockId, caller, _DEFAULT_LOCK_HOLD_MILLS);
    }

    /**
     * @param lockId 要确保不和其他业务冲突（不能用随机生成）
     * @param caller 业务处理器
     * @param timeout 超时时间（毫秒）
     * @return
     */
    public static <T> T execute(String lockId, LockCaller<T> caller, long timeout) {
        DistributeLock dLock = new DistributeLock(lockId);
        if (dLock.lock(timeout)) {
            try {
                return caller.onHolder();
            } finally {
                dLock.unlock();
            }
        } else {
            long start = System.currentTimeMillis();
            T result = null;
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
                if (!dLock.isLocked()) {
                    result = caller.onWait();
                    if (result != null)
                        return result;
                }
                if (System.currentTimeMillis() - start > timeout) {
                    throw new RuntimeException("DistributeLock[" + lockId + "] wait timeout");
                }
            }
        }
    }
}
