package com.fmi110.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fmi110.commons.result.PageBean;
import com.fmi110.mapper.SysLogMapper;
import com.fmi110.model.SysLog;
import com.fmi110.service.ISysLogService;
import org.apache.logging.log4j.core.appender.SyslogAppender;
import org.springframework.stereotype.Service;

import java.util.EventListener;

/**
 * 系统日志实现层
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper,SysLog> implements ISysLogService {

    /**
     * 分页查询
     *
     * @param pageBean
     * @return
     */
    @Override
    public PageBean selectDataGrid(PageBean pageBean) {

        Page<SysLog> page = new Page<SysLog>(pageBean.getNowpage(), pageBean.getSize());
        EntityWrapper<SysLog> wrapper = new EntityWrapper<>();
        wrapper.orderBy(pageBean.getSort(), pageBean.getOrder()
                                                    .equalsIgnoreCase("ASC"));
        super.selectPage(page, wrapper);
        pageBean.setRows(page.getRecords());
        pageBean.setTotal(page.getTotal());
        return pageBean;
    }
}
