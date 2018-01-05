package com.fmi110.controller;

import com.fmi110.commons.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录控制器,提供的方法包括 :
 *
 * 登录
 * 跳转首页
 * 退出登录
 *
 */
@Controller
public class LoginController extends BaseController{

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public Object loginPost(String username,String password,
                            @RequestParam(value="rememberMe",defaultValue = "0")Integer rememberMe){
        logger.info("POST请求登录");
        if (!StringUtils.isNotBlank(username)) {
            throw new RuntimeException("用户名不能为空");
        }
        if (!StringUtils.isNotBlank(password)) {
            throw new RuntimeException("密码不能为空");
        }

        /**
         * 还可以加入验证码...
         */

        Subject user = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 设置记住密码
        token.setRememberMe(1==rememberMe);

        try {
            user.login(token);
            return super.renderSuccess();
        } catch (UnknownAccountException e) {
            throw new RuntimeException("账号不存在！", e);
        } catch (DisabledAccountException e) {
            throw new RuntimeException("账号未启用！", e);
        } catch (IncorrectCredentialsException e) {
            throw new RuntimeException("密码错误！", e);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}
