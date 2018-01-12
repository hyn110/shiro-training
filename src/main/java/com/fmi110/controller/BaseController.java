package com.fmi110.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
        return getResultMap(true,"请求成功");
    }

    /**
     * 请求结果,数据封装在map中
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
}
