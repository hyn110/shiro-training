package com.fmi110.commons.shiro;

import com.fmi110.model.User;
import com.fmi110.model.vo.UserVo;
import com.fmi110.service.IRoleService;
import com.fmi110.service.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShiroDbRealm extends AuthorizingRealm{

    private static final Logger logger = LogManager.getLogger(ShiroDbRealm.class);

    @Autowired
    private IUserService userService;
    /**
     * 查询用户角色,获取用户权限信息
     */
    @Autowired
    private IRoleService roleService;

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
     * 获取授权(权限)信息
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();

        Map<String, Set<String>> resourceMap = roleService.selectResourceMapByUserId(user.getId());
        Set<String>              urls        = resourceMap.get("urls");
        Set<String>              roles       = resourceMap.get("roles");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        info.setRoles(roles); // 添加角色信息
        info.addStringPermissions(urls); // 添加权限信息

        return info;
    }

    /**
     * 获取认证信息 , 原理:
     * 1. 用户提交用户名和密码  login
     * 2. shiro 封装令牌
     * 3. reaml 通过用户名将密码查询返回
     * 4. shiro 比较查询回来的密码和用户输入的密码是否一致
     * 5. 进行登录控制
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {
        logger.info("shiro 开始权限验证 ShiroDbRealm.doGetAuthenticationInfo()");

        UsernamePasswordToken token =(UsernamePasswordToken)authenticationToken;
        String                username = token.getUsername();
        UserVo                userVo = new UserVo();
        userVo.setLoginName(username);
        List<User> list = userService.selectByLoginName(userVo);
        if (list == null || list.isEmpty()) {
            return null;
        }
        User user = list.get(0);
        if (user.getStatus() == 1) { // 帐号未启用
            return null;
        }

        /**
         * TODO 读取用户的 url 和 角色
         * 也可以在 doGetAuthorizationInfo() 实现
         */
        String salt = user.getSalt();
//        String credentialsSalt    = Base64.encodeToString(CodecSupport.toBytes(salt));
        return new SimpleAuthenticationInfo(user, user.getPassword()
                                                      .toCharArray(),
                                            ShiroByteSource.of(salt), getName());

    }

}
