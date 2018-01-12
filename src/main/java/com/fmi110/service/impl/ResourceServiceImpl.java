package com.fmi110.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fmi110.commons.result.Tree;
import com.fmi110.mapper.ResourceMapper;
import com.fmi110.mapper.RoleMapper;
import com.fmi110.mapper.RoleResourceMapper;
import com.fmi110.mapper.UserRoleMapper;
import com.fmi110.model.Resource;
import com.fmi110.model.User;
import com.fmi110.service.IResourceService;
import com.fmi110.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * resource(权限) 表的业务层接口实现
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {


    @Autowired
    private ResourceMapper     resourceMapper;
    @Autowired
    private UserRoleMapper     userRoleMapper;
    @Autowired
    private RoleMapper         roleMapper;
    @Autowired
    private RoleResourceMapper roleResourceMapper;
    @Autowired
    private IRoleService       roleService;

    /**
     * 获取所有的资源(权限),并排序
     *
     * @return
     */
    @Override
    public List<Resource> selectAll() {
        EntityWrapper<Resource> w = new EntityWrapper<>();
        w.orderBy("seq");
        return resourceMapper.selectList(w);
    }


    /**
     * 获取tree菜单 , 只返回一级菜单(所有父节点)
     * 使用场景 : 下拉列表 , 添加二级菜单时 , 需要选中父节点
     *
     * @return
     */
    @Override
    public List<Tree> selectAllMenu() {
        // 查询所有的菜单
        return resourceToTree("0");
    }

    /**
     * 获取所有菜单(包括父节点和子节点)
     *
     * @return
     */
    @Override
    public List<Tree> selectAllTree() {
        // 获取所有资源的 tree 形式,展示
        return resourceToTree(null);
    }

    /**
     * 查询指定用户的权限, 步骤 :
     * 用户 --> 角色 --> 权限
     *
     * @param user
     * @return
     */
    @Override
    public List<Tree> selectTree(User user) {

        List<Tree> trees = new ArrayList<>();
        if (user == null || user.getId() == null) {
            return trees;
        }

        Map<String, Set<String>> resourceMap = roleService.selectResourceMapByUserId(user.getId());
        Set<String>              urls        = resourceMap.get("urls");    // 用户能访问的url
        Set<String>              roles       = resourceMap.get("roles");  // 用户的角色信息
        // 情况1 : 超级管理员
        if (roles.contains("admin")) {
            List<Resource> resources = this.selectbyType("0");
            if (resources == null || resources.size() == 0) {
                return trees;
            }
            CopyPropertiesToTree(trees, resources);
            return trees;
        }

        // 情况2 : 普通用户
        List<Long> roleIds = userRoleMapper.selectRoleIdListByUserId(user.getId());
        if (roleIds == null || roleIds.size() == 0) {
            return trees;
        }

        List<Resource> resources = roleMapper.selectResourceListByRoleIdList(roleIds);

        CopyPropertiesToTree(trees, resources);

        return trees;
    }

    /**
     * 查询菜单
     *
     * @param type 菜单类型 , 0 一级菜单 , 1 二级菜单 , null 时返回所有
     * @return
     */
    public List<Resource> selectbyType(String type) {
        EntityWrapper<Resource> w = new EntityWrapper<>();
        w.orderBy("seq");

        if (type != null) {
            Resource resource = new Resource();
            w.setEntity(resource);
            w.addFilter("resource_type={0}", type);
        }
        return resourceMapper.selectList(w);
    }

    /**
     * 资源对象转换为Tree对象
     *
     * @param type
     * @return
     */
    private List<Tree> resourceToTree(String type) {
        List<Tree>     trees     = new ArrayList<>();
        List<Resource> resources = this.selectbyType(type);
        if (resources == null) {
            return trees;
        }
        CopyPropertiesToTree(trees, resources);
        return trees;
    }

    /**
     * 将 Resource 对象转成 Tree 对象放入集合
     *
     * @param trees
     * @param resources
     */
    private void CopyPropertiesToTree(List<Tree> trees, List<Resource> resources) {
        for (Resource resource : resources) {
            Tree t = new Tree();
            t.setId(resource.getId());
            t.setText(resource.getName());
            t.setPid(resource.getPid());
            t.setIconCls(resource.getIcon());
            t.setAttributes(resource.getUrl());
            t.setState(resource.getOpened());
            trees.add(t);
        }
    }


}
