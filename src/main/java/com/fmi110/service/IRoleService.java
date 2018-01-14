package com.fmi110.service;

import com.baomidou.mybatisplus.service.IService;
import com.fmi110.commons.result.PageBean;
import com.fmi110.model.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRoleService extends IService<Role> {
//    void selectDataGrid(PageBean pageInfo);

    /**
     * 返回所有的角色, 前端使用
     * @return
     */
    Object selectTree();
    /**
     * 根据 roleId(角色) 获取 resourceId(资源),
     * role 表 和 role_resource 表的联查
     * @param roleId
     * @return
     */
    List<Long> selectResourceIdListByRoleId(Long roleId);
    /**
     * 更新角色能访问的资源(权限) , 资源字符串以 "," 分割
     * @param roleId
     * @param resourceIds 资源的id,以 "," 拼接得到,如 "1,2,3,4"
     */
    void updateRoleResource(Long roleId, String resourceIds);
    /**
     * 根据用户ID 获取用户的所有角色 以及 能访问的资源的 url
     *
     * 涉及的表 : user --- user_role --- role --- role_resource --- resource
     * @param userId
     * @return  key=urls ---> 用户能访问的 url 集合
     *          key=roles ---> 用户具有的角色
     *      resourceMap.put("urls", urlSet);
     *      resourceMap.put("roles", roles);
     */
    Map<String, Set<String>> selectResourceMapByUserId(Long userId);

    /**
     * 分页查询角色 , 前端使用的数据
     * @param pageBean
     */
    PageBean selectDataGrid(PageBean pageBean);
}
