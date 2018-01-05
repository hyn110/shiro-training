package com.fmi110.commons.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class ShiroDbRealm extends AuthorizingRealm{

    /**
     * 注入缓存管理器和密码比较器
     * @param cacheManager
     * @param matcher
     */
    public ShiroDbRealm(CacheManager cacheManager,
                        CredentialsMatcher matcher) {
        super(cacheManager, matcher);
    }

    /**
     * 获取认证信息 , 原理:
     * 1. 用户提交用户名和密码  login
     * 2. shiro 封装令牌
     * 3. reaml 通过用户名将密码查询返回
     * 4. shiro 比较查询回来的密码和用户输入的密码是否一致
     * 5. 进行登录控制
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 获取授权(权限)信息
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        return null;
    }
}
