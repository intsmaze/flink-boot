package com.intsmaze.flink.lock;

import org.redisson.Redisson;
import org.redisson.core.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2021/07/10 18:33
 */
public class DistributedLock {

    static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    private List<RLock> locks;

    private String lockName;

    private List<Redisson> redissons;

    private int successCount;

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2021/07/10 18:33
     */
    public DistributedLock(List<Redisson> redissons, String lockName) {
        this.redissons = redissons;
        this.lockName = lockName;
        this.successCount = (this.redissons.size() / 2) + 1;
        this.locks = new ArrayList<RLock>(this.redissons.size());
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2021/07/10 18:33
     */
    public void lock() {
        Exception ex = null;
        for (Redisson redisson : redissons) {
            try {
                RLock lock = redisson.getLock(lockName);
                lock.lock();
                locks.add(lock);
            } catch (Exception e) {
                ex = e;
                logger.error("", e);
            }
        }
        if (locks.size() < successCount) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * github地址: https://github.com/intsmaze
     * 博客地址：https://www.cnblogs.com/intsmaze/
     * 出版书籍《深入理解Flink核心设计与实践原理》
     *
     * @auther: intsmaze(刘洋)
     * @date: 2021/07/10 18:33
     */
    public void unlock() {
        for (RLock lock : locks) {
            try {
                lock.unlock();
            } catch (Exception e) {
                logger.error("", e);
            }
        }

    }
}