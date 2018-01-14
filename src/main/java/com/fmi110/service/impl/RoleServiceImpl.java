package com.fmi110.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fmi110.commons.result.PageBean;
import com.fmi110.commons.result.Tree;
import com.fmi110.mapper.RoleMapper;
import com.fmi110.mapper.RoleResourceMapper;
import com.fmi110.mapper.UserRoleMapper;
import com.fmi110.model.Role;
import com.fmi110.model.RoleResource;
import com.fmi110.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 角色表服务层接口实现类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements IRoleService {
    /**
     * 角色表操作
     */
    @Autowired
    private RoleMapper roleMapper;
    /**
     * 用户角色表操作
     */
    @Autowired
    private UserRoleMapper userRoleMapper;
    /**角色资源表操作*/
    @Autowired
    private RoleResourceMapper roleResourceMapper;


    @Override
    public Object selectTree() {

        List<Tree> trees = new ArrayList<>();
        List<Role> roles = this.selectAll();
        for (Role role : roles) {
            Tree tree = new Tree();
            tree.setId(role.getId());
            tree.setText(role.getName());
            trees.add(tree);
        }
        return trees;
    }

    /**
     * 查询所有的角色,按 "seq" 排序
     * @return
     */
    public List<Role> selectAll() {
        EntityWrapper<Role> wrapper = new EntityWrapper<>();
        wrapper.orderBy("seq");
        return this.selectList(wrapper);
    }

    /**
     * 根据 roleId(角色) 获取 resourceId(资源)
     * role 表 和 role_resource 表的联查
     * @param id
     * @return
     */
    @Override
    public List<Long> selectResourceIdListByRoleId(Long id) {
        return roleMapper.selectResourceIdListByRoleId(id);
    }

    /**
     * 更新角色能访问的资源(权限) , 资源字符串以 "," 分割
     * @param roleId
     * @param resourceIds 资源的id,以 "," 拼接得到,如 "1,2,3,4"
     */
    @Override
    public void updateRoleResource(Long roleId, String resourceIds) {
        /**
         * 更新思路: 先全部删除旧权限,然后重新添加
         */
        RoleResource roleResource = new RoleResource();
        roleResource.setRoleId(roleId);
        // 根据roleId 字段删除
        roleResourceMapper.delete(new EntityWrapper<RoleResource>(roleResource));

        if (StringUtils.isEmpty(resourceIds)) {
            return; // 权限字符为空,说明是取消所有权限,直接返回
        }
        String[] ids = resourceIds.split(",");
        for(String id:ids){
            roleResource = new RoleResource();
            roleResource.setRoleId(roleId);
            roleResource.setResourceId(Long.parseLong(id));
            roleResourceMapper.insert(roleResource);
        }
    }

    /**
     * 根据用户ID 获取用户的所有角色 以及 能访问的资源的 url
     *
     * 涉及的表 : user --- user_role --- role --- role_resource --- resource
     * @param userId
     * @return
     */
    @Override
    public Map<String, Set<String>> selectResourceMapByUserId(Long userId) {

        Map<String, Set<String>> resourceMap = new HashMap<>();
        /**
         * 1 查出用户对应的所有 角色id
         * 2 根据角色id 查询出所有对应的 resource
         */
        List<Long> roleIdList = userRoleMapper.selectRoleIdListByUserId(userId);
        // 资源url
        Set<String> urlSet = new HashSet<>();
        Set<String> roles = new HashSet<>();

        for (Long roleId : roleIdList) {
            List<Map<Long, String>> resourceList = roleMapper.selectResourceListByRoleId(roleId);
            if(resourceList!=null) {
                for (Map<Long, String> map : resourceList) {
                    if(!StringUtils.isEmpty(map.get("url"))){
                        urlSet.add(map.get("url"));
                    }
                }
            }
            Role role = roleMapper.selectById(roleId);
            if (role != null) {
                roles.add(role.getName());
            }
        }

        resourceMap.put("urls", urlSet);
        resourceMap.put("roles", roles);

        return resourceMap;
    }

    /**
     * 分页查询角色
     *
     * @param pageBean
     */
    @Override
    public PageBean selectDataGrid(PageBean pageBean) {
        Page<Role> page = new Page<>(pageBean.getNowpage(), pageBean.getSize());
        EntityWrapper<Role> wrapper = new EntityWrapper<>();
        wrapper.orderBy(pageBean.getSort(), pageBean.getOrder()
                                                    .equalsIgnoreCase("ASC"));

        super.selectPage(page);
        pageBean.setRows(page.getRecords());
        pageBean.setTotal(page.getTotal());
        return pageBean;
    }
}
