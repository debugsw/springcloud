package com.springcloud.base.core.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ls
 * @Description: 自旋锁
 * @Date: 2023/4/10 14:41
 */
@Slf4j
public class RedisSpinLock {

    private static final String REDIS_SPIN_LOCK = "_REDIS_SPIN_LOCK:";

    private final StringRedisTemplate stringRedisTemplate;

    private final String redisLockKey;

    public static final StringRedisSerializer UTF_8 = new StringRedisSerializer(StandardCharsets.UTF_8);

    /**
     * 锁的关键字
     */
    private String redisLockValue;

    /**
     * 是否持有锁
     */
    private boolean handleLock;

    public static RedisSpinLock newInstance(StringRedisTemplate stringRedisTemplate, String redisLockKey) {
        return new RedisSpinLock(stringRedisTemplate, redisLockKey);
    }

    private RedisSpinLock(StringRedisTemplate stringRedisTemplate, String redisLockKey) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisLockKey = REDIS_SPIN_LOCK + redisLockKey;
    }

    /**
     * redis单点获取锁 线程sleep获取自旋锁 注意retryTimes设置的合理性
     *
     * @param timeout    锁定时间 毫秒
     * @param sleepTime  自旋间隔时间 毫秒
     * @param retryTimes 尝试次数 >0  小于0的时候默认1
     * @return 是否获取成功
     */
    public Boolean lock(long timeout, long sleepTime, int retryTimes) {

        
        
        retryTimes = retryTimes <= 0 ? 1 : retryTimes;

        
        if (this.handleLock) {
            this.unLock();
        }

        
        this.handleLock = false;
        this.redisLockValue = UUID.randomUUID().toString();

        for (int i = 0; i < retryTimes; i++) {
            Boolean lock = this.stringRedisTemplate.opsForValue().setIfAbsent(this.redisLockKey, this.redisLockValue, timeout, TimeUnit.MILLISECONDS);
            if (lock != null && lock) {
                log.debug("第{}次获取自旋锁成功key:{}", i + 1, this.redisLockKey);
                this.handleLock = true;
                return true;
            }

            
            
            if (sleepTime != 0 && i < retryTimes - 1) {
                
                try {
                    log.debug("第{}次获取自旋锁失败，进入睡眠，睡眠时间{}ms,key:{}", i + 1, sleepTime, this.redisLockKey);
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ignored) {
                }
            }
        }
        
        log.warn("自旋结束，获取redis自旋锁失败key : {}", this.redisLockKey);
        return false;
    }

    /**
     * redis单点获取锁 获取失败则返回false
     *
     * @param timeout 锁定时间
     * @return 是否成功获取锁
     */
    public Boolean lock(long timeout) {
        return this.lock(timeout, 0, 0);
    }

    /**
     * 尝试获取锁，获取方式为非自旋的方式
     * 只会尝试一次，并且不会等待
     *
     * @param timeout 锁定时间
     * @return 返回获取的结果
     */
    public Boolean tryLock(long timeout) {
        return this.lock(timeout, 0, 1);
    }

    /**
     * 自旋锁解锁
     */
    public void unLock() {
        if (StringUtils.hasText(this.redisLockValue)) {
            String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            stringRedisTemplate.execute((RedisCallback<Boolean>) connection -> {
                connection.eval(Objects.requireNonNull(UTF_8.serialize(luaScript)),
                        ReturnType.INTEGER,
                        1,
                        UTF_8.serialize(this.redisLockKey),
                        UTF_8.serialize(this.redisLockValue));
                return true;
            });
        }
    }

}
