package com.fmi110.commons.shiro.cache;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.cache.CacheException;
import org.springframework.cache.Cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * 缓存
 * 使用 spring-cache 作为缓存
 * @param <K>
 * @param <V>
 */
public class ShiroSpringCache<K, V> implements org.apache.shiro.cache.Cache<K, V> {

    private static final Logger logger = LogManager.getLogger(ShiroSpringCache.class);

    private final org.springframework.cache.Cache cache;

    public ShiroSpringCache(Cache cache) {
        if (cache == null) {
            throw new IllegalArgumentException("缓存对象 Cache 不能为 null...");
        }
        this.cache = cache;
    }


    @Override
    public V get(K k) throws CacheException {
        V                  v            = null;
        Cache.ValueWrapper valueWrapper = cache.get(k);
        if (null == valueWrapper) {
            v = null;
            if (logger.isTraceEnabled()) {
                logger.trace("从缓存 [" + this.cache.getName() + "] " +
                             "获取 {key : " + k + " , value : " + v + "}");
            }
            return v;
        }
        v = (V) valueWrapper.get();

        if (logger.isTraceEnabled()) {
            logger.trace("从缓存 [" + this.cache.getName() + "] " +
                         "获取 {key : " + k + " , value : " + v + "}");
        }

        return v;
    }

    /**
     * 存入新的值,返回上一次缓存的值
     *
     * @param k
     * @param v
     * @return
     * @throws CacheException
     */
    @Override
    public V put(K k, V v) throws CacheException {
        V pre = get(k);
        cache.put(k, v);
        if (logger.isTraceEnabled()) {
            logger.trace("往缓存 [" + this.cache.getName() + "] " +
                         "存入 {key : " + k + " , value : " + v + "}");
        }
        return pre;
    }

    @Override
    public V remove(K k) throws CacheException {
        V previous = get(k);
        cache.evict(k);
        if (logger.isTraceEnabled()) {
            logger.trace("从缓存 [" + this.cache.getName() + "]中" +
                         "移除 {key : " + k + " , value : " + previous + "}");
        }
        return previous;
    }

//    public static void main(String[] ss){
//        logger.trace("移除 {}","aa");
//    }

    @Override
    public void clear() throws CacheException {
        if (logger.isTraceEnabled()) {
            logger.trace("清空缓存 [" + this.cache.getName() + "] " );
        }
        cache.clear();
    }

    @Override
    public int size() {
        return 0;
    }

    /**
     * 返回一个空集合
     *
     * @return
     */
    @Override
    public Set<K> keys() {
        return Collections.emptySet();
    }

    /**
     * 返回一个空集合
     *
     * @return
     */
    @Override
    public Collection<V> values() {
        return Collections.emptySet();
    }

    @Override
    public String toString() {
        return "ShiroSpringCache [" + this.cache.getName() + "]";
    }
}
