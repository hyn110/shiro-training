package com.fmi110.controller;

import com.fmi110.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseController {
    /**
     * 日志
     */
    protected Logger logger = LogManager.getLogger(getClass());
    @InitBinder
    public void initBinder(ServletRequestDataBinder binder){
        /**
         * 在这里定制一些设置,比如注册日期转换器等...
         */
    }

    /**
     * ajax请求成功
     * @return
     */
    public Object renderSuccess() {
        return getResultMap("200","请求成功");
    }

    /**
     * 请求结果,数据封装在map中 , 数据转为json时的格式为 : {status:status,data:data}
     * @param status
     * @param data
     * @return
     */
    public Map<String,Object> getResultMap(Object status,Object data){
        HashMap<String, Object> map = new HashMap<>();
        map.put("status",status);
        map.put("data", data);
        return map;
    }

    /**
     * 获取当前登录的用户
     * 用户在shiro进行认证时 shiroDbReaml.doGetAuthenticationInfo 保存到shiro
     * @return
     */
    public User getLogginUser(){
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 获取当前登录用户的ID
     * @return
     */
    public Long getLogginUserId(){
        return this.getLogginUser().getId();
    }
}
