package com.fmi110.service;

import com.baomidou.mybatisplus.service.IService;
import com.fmi110.model.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRoleService extends IService<Role> {
//    void selectDataGrid(PageInfo pageInfo);

    Object selectTree();

    List<Long> selectResourceIdListByRoleId(Long id);

    void updateRoleResource(Long roleId, String resourceIds);

    Map<String, Set<String>> selectResourceMapByUserId(Long userId);
}
