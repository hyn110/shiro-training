package com.fmi110.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

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
        return "success";
    }
}
