package cn.com.warlock.common2.lock;

import static cn.com.warlock.cache.redis.JedisProviderFactory.getJedisCommands;
import static cn.com.warlock.cache.redis.JedisProviderFactory.getJedisProvider;

/**
 * 基于redis的分布式锁
 */
public class DistributeLock {

    private static final String LOCKED                   = "locked";
    private static final long   _DEFAULT_LOCK_HOLD_MILLS = 30000;
    private String              lockId;

    /**
     * 
     * @param lockId 要确保不和其他业务冲突（不能用随机生成）
     */
    public DistributeLock(String lockId) {
        super();
        this.lockId = lockId;
    }

    /**
     * 获取锁，默认锁定30秒
     * @return
     */
    public boolean lock() {
        return lock(_DEFAULT_LOCK_HOLD_MILLS);
    }

    /**
     * 获取锁lock
     * @param lockId
     * @param timeout 毫秒
     * @return 获得lock ＝＝ true  
     */
    public boolean lock(long timeout) {
        try {
            boolean res = getJedisCommands(null).setnx(lockId, LOCKED) > 0;
            getJedisCommands(null).pexpire(lockId, timeout);
            return res;
            //			String lock = getJedisCommands(null).getSet(lockId, UUIDUtils.uuid());
            //			return StringUtils.isBlank(lock);
        } finally {
            getJedisProvider(null).release();
        }
    }

    public void unlock() {
        try {
            getJedisCommands(null).del(lockId);
        } finally {
            getJedisProvider(null).release();
        }
    }

    public boolean isLocked() {
        try {
            return getJedisCommands(null).exists(lockId);
        } finally {
            getJedisProvider(null).release();
        }

    }
}
