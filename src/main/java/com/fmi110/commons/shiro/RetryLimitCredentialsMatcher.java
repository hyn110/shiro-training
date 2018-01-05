package com.fmi110.commons.shiro;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 输错五次密码锁定半小时
 * 输入次数保存在缓存中,缓存时间为半小时,通过缓存超时时间来实现时间控制
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher implements InitializingBean{

    private final static Logger logger             = LogManager.getLogger(RetryLimitCredentialsMatcher.class);
    private final static String DEFAULT_CACHE_NAME = "retryLimitCache";

    private final CacheManager cacheManager;
    private  String retryLimitCacheName = DEFAULT_CACHE_NAME;
    /**
     * 记录输入次数
     */
    private Cache<String, AtomicInteger> passwordRetryCache;

    public String getRetryLimitCacheName() {
        return retryLimitCacheName;
    }

    public void setRetryLimitCacheName(String retryLimitCacheName) {
        this.retryLimitCacheName = retryLimitCacheName;
    }

    public RetryLimitCredentialsMatcher(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        String username = (String) token.getPrincipal();

        AtomicInteger retryCount = passwordRetryCache.get(username);

        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        if (retryCount.incrementAndGet() > 5) {
            logger.warn("用户["+username+"]尝试登录次数错误超过5次,账户锁定半小时");
            throw new ExcessiveAttemptsException("用户["+username+"]尝试登录次数错误超过5次,账户锁定半小时");
        }else{
            passwordRetryCache.put(username, retryCount);
        }

        boolean matches =  super.doCredentialsMatch(token, info);
        if (matches) {
            // 清除次数记录
            passwordRetryCache.remove(username);
        }
        return matches;
    }

    /**
     * 初始化
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        /**
         * 初始化缓存器
         */
        this.passwordRetryCache = cacheManager.getCache(retryLimitCacheName);
    }
}
