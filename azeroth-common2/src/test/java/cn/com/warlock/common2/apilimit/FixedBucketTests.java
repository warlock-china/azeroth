package cn.com.warlock.common2.apilimit;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import cn.com.warlock.common2.apilimit.FixedBucket;
import cn.com.warlock.common2.apilimit.Token;
import cn.com.warlock.common2.apilimit.TokenStore;

public abstract class FixedBucketTests {

    @Test
    public void sequentialFixedBucketAccess() {
        FixedBucket rateLimiter = new FixedBucket();
        int allowedRequests = 1;
        rateLimiter.setAllowedRequests(allowedRequests);
        rateLimiter.setTokenStore(createTokenStore());
        rateLimiter.init();

        RateLimiterKey key = new RateLimiterKey();

        Token token = rateLimiter.getToken(key);
        assertTrue("获取第一个请求的token", token.isUsable());

        token = rateLimiter.getToken(key);

        assertFalse("再消费一次就不好使咯", token.isUsable());
    }

    @Test
    public void canDoReasonableNumberOfTokenChecksPerSecond() throws Exception {
        FixedBucket rateLimiter = new FixedBucket();
        int allowedRequests = 50000;
        rateLimiter.setAllowedRequests(allowedRequests);
        rateLimiter.setTokenStore(createTokenStore());
        rateLimiter.init();

        RateLimiterKey key = new RateLimiterKey();

        Token token;

        for (int i = 0; i < allowedRequests; i++) {
            token = rateLimiter.getToken(key);
            assertTrue("验证请求的token是否ok", token.isUsable());
        }

        token = rateLimiter.getToken(key);

        assertFalse("再来一个就不可用了，定义了 " + allowedRequests + " token 不到一秒就可以执行完", token.isUsable());
    }

    /**
     * multipleClientsCanAccessWithoutBlocking: <br/>
     *
     * @author warlock
     * 多个客户端一样可以起飞没有阻塞
     * @throws Exception
     * @since v1.0.0
     */
    @Test
    public void multipleClientsCanAccessWithoutBlocking() throws Exception {
        final FixedBucket rateLimiter = new FixedBucket();
        int allowedRequests = 200;
        rateLimiter.setAllowedRequests(allowedRequests);
        rateLimiter.setTokenStore(createTokenStore());
        rateLimiter.init();

        final RateLimiterKey key = new RateLimiterKey();

        int clientCount = allowedRequests;
        Runnable[] clients = new Runnable[clientCount];
        final boolean[] isUsable = new boolean[clientCount];

        final CountDownLatch startGate = new CountDownLatch(1);

        final CountDownLatch endGate = new CountDownLatch(clientCount);

        for (int i = 0, n = isUsable.length; i < n; ++i) {
            final int j = i;
            clients[j] = new Runnable() {

                public void run() {
                    try {
                        startGate.await();

                        isUsable[j] = rateLimiter.getToken(key).isUsable();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        endGate.countDown();
                    }
                }
            };
        }

        ExecutorService executor = Executors.newFixedThreadPool(clientCount);

        for (Runnable runnable : clients) {
            executor.execute(runnable);
        }

        startGate.countDown();

        endGate.await();

        for (boolean b : isUsable) {
            assertTrue("验证Token 可用", b);
        }
    }

    @Test
    public void expiryOfTokensIsSupported() throws Exception {
        FixedBucket rateLimiter = new FixedBucket();
        int allowedRequests = 50;
        rateLimiter.setAllowedRequests(allowedRequests);
        rateLimiter.setTokenStore(createTokenStore());
        rateLimiter.setDuration(1);
        rateLimiter.init();

        RateLimiterKey key = new RateLimiterKey();

        Token token = rateLimiter.getToken(key);
        assertTrue("验证 token", token.isUsable());

        // 上面设置了保持1s，这里让它过期
        Thread.sleep(1001);

        token = rateLimiter.getToken(key);
        assertTrue("验证 token", token.isUsable());
    }

    protected abstract TokenStore createTokenStore();

}
