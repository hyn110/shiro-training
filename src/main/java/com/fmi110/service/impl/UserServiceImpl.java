package com.fmi110.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fmi110.commons.result.PageBean;
import com.fmi110.commons.utils.BeanUtils;
import com.fmi110.commons.utils.StringUtils;
import com.fmi110.mapper.UserMapper;
import com.fmi110.mapper.UserRoleMapper;
import com.fmi110.model.User;
import com.fmi110.model.UserRole;
import com.fmi110.model.vo.UserVo;
import com.fmi110.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<User> selectByLoginName(UserVo userVo) {
        User user = new User();
        user.setLoginName(userVo.getLoginName());
        EntityWrapper<User> wrapper = new EntityWrapper<>(user);
        if (null != userVo.getId()) {
            wrapper.where("id!={0}", userVo.getId());
        }
        return this.selectList(wrapper);
    }

    @Override
    public UserVo selectVoById(Long id) {
        return userMapper.selectUserVoById(id);
    }

    /**
     * 带条件筛选的分页查找
     *
     * @param pageBean
     * @return
     */
    @Override
    public PageBean selectDataGrid(PageBean pageBean) {
        Page<Map<String, Object>> page = new Page<>(pageBean.getNowpage(), pageBean.getSize());
        page.setOrderByField(pageBean.getSort());
        page.setAsc(pageBean.getOrder()
                            .equalsIgnoreCase("asc"));
        List<Map<String, Object>> list = userMapper.selectUserPage(page, pageBean.getCondition());
        pageBean.setRows(list);
        pageBean.setTotal(page.getTotal());
        return pageBean;
    }

    @Override
    public void insertByVo(UserVo userVo) {
        User user = BeanUtils.copy(userVo, User.class);
        user.setCreateTime(new Date());
        super.insert(user);

        // 维护用户--角色
        Long id = user.getId();
        String[] roles = userVo.getRoleIds().split(",");
        UserRole userRole = new UserRole();
        for (String roleId : roles) {
            userRole.setUserId(id);
            userRole.setRoleId(Long.valueOf(roleId));
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public void updateByVo(UserVo userVo) {
        User user = BeanUtils.copy(userVo,User.class);
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null); // 密码置空后不会执行修改该列
        }
        super.updateById(user);

        // 更新 用户 --- 角色 : 先删除再添加
        Long id = userVo.getId();
        List<UserRole> userRoles = userRoleMapper.selectByUserId(id);
        // 删除
        if (userRoles != null && userRoles.size() > 0) {
            for (UserRole userRole : userRoles) {
                userRoleMapper.deleteById(userRole.getId());
            }
        }
        // 添加
        String[] roles = userVo.getRoleIds().split(",");
        UserRole userRole = new UserRole();
        for (String roleId : roles) {
            userRole.setUserId(id);
            userRole.setRoleId(Long.valueOf(roleId));
            userRoleMapper.insert(userRole);
        }
    }

    /**
     * 更新密码
     * @param userId
     * @param md5Hex 加密后的密码
     */
    @Override
    public void updatePwdByUserId(Long userId, String md5Hex) {
        User user = new User();
        user.setId(userId);
        user.setPassword(md5Hex);
        super.updateById(user);
    }

    @Override
    public void deleteUserById(Long id) {
        super.deleteById(id);
    }
}
