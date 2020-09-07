package com.xycode.distributed;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author xycode
 */
@Slf4j
public class RedisDistributedLock implements Lock {
    private String lockName;

    private Jedis cli;

    public RedisDistributedLock(String lockName) {
        this.lockName = lockName;
        cli = new Jedis("121.48.165.121", 7777);
        cli.auth("pass");
    }

    @Override
    public void lock() {
        while (true) {
            if (tryLock()) {
                break;
            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("{} waiting for redis lock", Thread.currentThread().getName());
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        if (cli.setnx(lockName, "lock") == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        cli.del(lockName);
        log.info("{} released redis lock", Thread.currentThread().getName());
    }


    @Override
    public Condition newCondition() {
        return null;
    }
}
