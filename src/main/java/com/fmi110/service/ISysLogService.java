package com.fmi110.service;

import com.baomidou.mybatisplus.service.IService;
import com.fmi110.commons.result.PageBean;
import com.fmi110.model.SysLog;

/**
 * 系统日志业务层
 */
public interface ISysLogService extends IService<SysLog>{
    /**
     * 分页查询
     * @param pageBean
     * @return
     */
    PageBean selectDataGrid(PageBean pageBean);
}
