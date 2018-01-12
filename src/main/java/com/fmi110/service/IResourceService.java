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

    List<Resource> selectAll();

    List<Tree> selectAllMenu();

    List<Tree> selectAllTree();

    List<Tree> selectTree(User user);
}
