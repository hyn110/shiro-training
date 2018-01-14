package com.fmi110.controller;

import com.fmi110.commons.utils.StringUtils;
import com.fmi110.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录控制器,提供的方法包括 :
 * <p>
 * 登录
 * 跳转首页
 * 退出登录
 */
@Controller
public class LoginController extends BaseController {

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping(value = "/login",produces = "application/json")
    @ResponseBody
    public Object loginPost(String username, String password,
                            @RequestParam(value = "rememberMe", defaultValue = "0") Integer rememberMe) {
        logger.info("POST请求登录");
        if (!StringUtils.isNotBlank(username)||!StringUtils.isNotBlank(password)) {
            return "{}";
        }
//        if (!StringUtils.isNotBlank(username)) {
//            throw new RuntimeException("用户名不能为空");
//        }
//        if (!StringUtils.isNotBlank(password)) {
//            throw new RuntimeException("密码不能为空");
//        }

        /**
         * 还可以加入验证码...
         */

        Subject               user  = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        // 设置记住密码
        token.setRememberMe(1 == rememberMe);

        try {
            user.login(token);
            return super.renderSuccess();
        }catch (Exception e){
            e.printStackTrace();
            return super.getResultMap("error", "用户名或密码错误....");
        }
//        } catch (UnknownAccountException e) {
//            throw new RuntimeException("账号不存在！", e);
//        } catch (DisabledAccountException e) {
//            throw new RuntimeException("账号未启用！", e);
//        } catch (IncorrectCredentialsException e) {
//            throw new RuntimeException("密码错误！", e);
//        } catch (Throwable e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
    }

    /**
     * 重定向到 /index 请求
     *
     * @return
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/index";
    }

    /**
     * 返回 index.html 页面
     */
    @GetMapping("/index")
    public String index1() {
        return "/index";
    }

    @GetMapping("/login")
    public String login() {
        logger.info("GET 登录请求");
        // 用户在当前会话中已认证
        if (SecurityUtils.getSubject()
                         .isAuthenticated()) {
            return "redirect:/index";
        }
        return "/login";
    }

    /**
     * 返回当前用户的登录名
     * @return
     */
    @GetMapping("/showName")
    @ResponseBody
    public Object showName() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return super.getResultMap("200",user.getLoginName());
    }

    /**
     * 退出登录
     *
     * @return
     */
    @PostMapping("/logout")
    public Object logout() {
        logger.info("登出");
        SecurityUtils.getSubject()
                     .logout();
        return super.getResultMap("200", "success");
    }

    /**
     * 未授权时跳转处理
     */
    @GetMapping("/unauth")
    public String unauth() {
        if (!SecurityUtils.getSubject()
                          .isAuthenticated()) {
            return "redirect:/login"; // 未登录未授权过,重定向到登录页面
        }
        return "unauth";
    }
}
