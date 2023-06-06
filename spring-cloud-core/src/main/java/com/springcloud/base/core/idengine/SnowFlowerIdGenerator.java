package com.springcloud.base.core.idengine;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ls
 * @Description: 雪花算法
 * @Date: 2023/4/10 09:53
 */
@SuppressWarnings("all")
public class SnowFlowerIdGenerator {

    /**
     * Start time intercept (Thu, 04 Nov 2010 01:42:54 GMT)
     */
    public static final long EPOCH = 1288834974657L;
    
    private static final long SEQUENCE_BITS = 12L;
    
    private static final long WORKER_ID_BITS = 10L;
    
    private static final long SEQUENCE_MASK = 4095L;
    
    private static final long WORKER_ID_LEFT_SHIFT_BITS = 12L;
    
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = 22L;
    
    private static final long WORKER_ID_MAX_VALUE = 1024L;
    private long workerId;
    private long sequence;
    private long lastTime;
    private long currentId;


    public synchronized long nextId() {
        long currentMillis = System.currentTimeMillis();
        Preconditions.checkState(this.lastTime <= currentMillis,
                "Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds",
                new Object[]{this.lastTime, currentMillis});
        if (this.lastTime == currentMillis) {
            if (0L == (this.sequence = ++this.sequence & 4095L)) {
                currentMillis = this.waitUntilNextTime(currentMillis);
            }
        } else {
            this.sequence = 0L;
        }

        this.lastTime = currentMillis;
        this.currentId = currentMillis - EPOCH << 22 | workerId << 12 | this.sequence;

        return currentId;
    }

    public Map<Object, Object> info() {
        Map<Object, Object> info = new HashMap<>(4);
        info.put("currentId", currentId);
        info.put("workerId", workerId);
        return info;
    }

    public void initialize(long workerId) {
        if (workerId > WORKER_ID_MAX_VALUE || workerId < 0) {
            throw new IllegalArgumentException(String.format(
                    "worker Id can't be greater than %d or less than 0, current workId %d",
                    WORKER_ID_MAX_VALUE, workerId));
        }
        this.workerId = workerId;
    }

    private long waitUntilNextTime(long lastTimestamp) {
        long time = System.currentTimeMillis();
        while (time <= lastTimestamp) {
            time = System.currentTimeMillis();
        }
        return time;
    }
}
