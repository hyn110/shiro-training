package com.fmi110.service;

import com.baomidou.mybatisplus.service.IService;
import com.fmi110.commons.result.Tree;
import com.fmi110.model.Resource;
import com.fmi110.model.User;

import java.util.List;

/**
 * 资源表的操作
 * 这里资源,即指 url 资源,能访问资源就是用户菜单下能看到对应的的url
 */
public interface IResourceService extends IService<Resource> {
    /**
     * 获取所有的资源(权限),并排序
     *
     * @return
     */
    List<Resource> selectAll();
    /**
     * 获取tree菜单 , 只返回一级菜单(所有父节点)
     * 使用场景 : 下拉列表 , 添加二级菜单时 , 需要选中父节点
     *
     * @return
     */
    List<Tree> selectAllMenu();
    /**
     * 获取所有菜单(包括父节点和子节点)
     *
     * @return
     */
    List<Tree> selectAllTree();
    /**
     * 查询指定用户的权限, 步骤 :
     * 用户 --> 角色 --> 权限
     *
     * @param user
     * @return
     */
    List<Tree> selectTree(User user);
}
