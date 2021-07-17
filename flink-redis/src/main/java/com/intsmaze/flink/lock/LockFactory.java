package com.intsmaze.flink.lock;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.redisson.Config;
import org.redisson.Redisson;
import org.redisson.SingleServerConfig;

import java.util.List;

/**
 * github地址: https://github.com/intsmaze
 * 博客地址：https://www.cnblogs.com/intsmaze/
 * 出版书籍《深入理解Flink核心设计与实践原理》
 *
 * @auther: intsmaze(刘洋)
 * @date: 2021/07/10 18:33
 */
public class LockFactory {

    private List<String> addressList = null;
    private String clientName = null;
    private Integer connectionPoolSize = 3;
    private Integer retryAttempts = 0;
    private Integer retryInterval = 10;
    private Integer timeout = 2000;
    private Integer pingTimeout = 500;
    private Integer failAttemptsAmount = -1;
    private String password;
    private List<Redisson> redissons;

    public Config getRedissonSingleServerConfig() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer();
        serverConfig.setDatabase(14);
        serverConfig.setPingTimeout(pingTimeout);
        serverConfig.setRefreshConnectionAfterFails(failAttemptsAmount);
        serverConfig.setConnectionPoolSize(connectionPoolSize);
        serverConfig.setRetryAttempts(retryAttempts);
        serverConfig.setRetryInterval(retryInterval);
        serverConfig.setTimeout(timeout);
        if (clientName != null) {
            serverConfig.setClientName(clientName);
        }
        if (password != null) {
            serverConfig.setPassword(password);
        }
        return config;
    }

    public List<Redisson> createRedissons() {
        Preconditions.checkArgument((addressList != null && !addressList.isEmpty()), "未定义addressList列表");
        List<Redisson> addrGroup = Lists.newArrayListWithCapacity(addressList.size());
        for (String addr : addressList) {
            Config config = getRedissonSingleServerConfig();
            config.useSingleServer().setAddress(addr);
            addrGroup.add(Redisson.create(config));
        }
        return addrGroup;
    }

    public DistributedLock newLock(String name) {
        DistributedLock lock = new DistributedLock(this.redissons, name);
        return lock;
    }

    public void init() {
        this.redissons = createRedissons();
    }

    public List<String> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }

    public List<Redisson> getRedissons() {
        return redissons;
    }

    public void setRedissons(List<Redisson> redissons) {
        this.redissons = redissons;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setConnectionPoolSize(Integer connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public void setRetryAttempts(Integer retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public void setPingTimeout(Integer pingTimeout) {
        this.pingTimeout = pingTimeout;
    }

    public void setFailAttemptsAmount(Integer failAttemptsAmount) {
        this.failAttemptsAmount = failAttemptsAmount;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
