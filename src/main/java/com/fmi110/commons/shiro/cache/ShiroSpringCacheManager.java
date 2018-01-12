package com.fmi110.commons.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;

/**
 * 缓存管理器
 * 使用 spring-cache 作为shiro缓存
 */
public class ShiroSpringCacheManager implements CacheManager,Destroyable{
    /**
     * 使用 spring 提供的 CacheManager ,方便整合
     */
    private org.springframework.cache.CacheManager cacheManager;

    public void setCacheManager(org.springframework.cache.CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public org.springframework.cache.CacheManager getCacheManager() {
        return cacheManager;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        org.springframework.cache.Cache cache = cacheManager.getCache(name);
        return new ShiroSpringCache<K, V>(cache);
    }

    @Override
    public void destroy() throws Exception {
        cacheManager = null;
    }
}
