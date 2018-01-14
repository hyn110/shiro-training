package com.fmi110.service;

import com.baomidou.mybatisplus.service.IService;
import com.fmi110.commons.result.Tree;
import com.fmi110.model.Organization;

import java.util.List;

/**
 * 部门表 服务层接口
 */
public interface IOrganizationService extends IService<Organization> {
    /**前端用的树数据*/
    List<Tree> selectTree();

    List<Organization> selectTreeGrid();
}
