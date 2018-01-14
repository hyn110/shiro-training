package com.fmi110.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fmi110.mapper.RoleResourceMapper;
import com.fmi110.model.RoleResource;
import com.fmi110.service.IRoleResourceService;
import org.springframework.stereotype.Service;

/**
 * 角色权限(资源)服务层接口
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper,RoleResource> implements
                                                                                          IRoleResourceService{
}
