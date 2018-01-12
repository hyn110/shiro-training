package com.fmi110.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmi110.model.Resource;
import com.fmi110.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * Role 表数据库控制层接口
 *
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 根据角色 id 获取所有的资源id
     * @param id
     * @return
     */
    List<Long> selectResourceIdListByRoleId(@Param("id") Long id);

    /**
     * 查询给定的角色集合所有的权限
     * @param list
     * @return
     */
    List<Resource> selectResourceListByRoleIdList(@Param("list") List<Long> list);

    /**
     * 查询指定角色具有的权限
     * @param id
     * @return
     */
    List<Map<Long, String>> selectResourceListByRoleId(@Param("id") Long id);

}