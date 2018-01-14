package com.fmi110.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fmi110.mapper.UserRoleMapper;
import com.fmi110.model.UserRole;
import com.fmi110.service.IUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色中间表业务层
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper,UserRole> implements IUserRoleService {
}
