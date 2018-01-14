package com.fmi110.controller;

import com.fmi110.commons.result.PageBean;
import com.fmi110.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @description：日志管理
 * @author：zhixuan.wang
 * @date：2015/10/30 18:06
 */
@Controller
@RequestMapping("/sysLog")
public class SysLogController extends BaseController{
    @Autowired
    private ISysLogService sysLogService;

    @GetMapping("/manager")
    public String manager() {
        return "admin/syslog";
    }

    @PostMapping("/dataGrid")
    @ResponseBody
    public PageBean dataGrid(Integer page, Integer rows,
                             @RequestParam(value = "sort", defaultValue = "create_time") String sort,
                             @RequestParam(value = "order", defaultValue = "DESC") String order) {
        PageBean pageBean = new PageBean(page, rows, sort, order);
        return sysLogService.selectDataGrid(pageBean);
    }
}
