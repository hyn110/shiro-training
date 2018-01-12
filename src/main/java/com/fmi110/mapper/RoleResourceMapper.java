package com.fmi110.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fmi110.model.RoleResource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.io.Serializable;

/**
 * RoleResource 表数据库控制层接口
 */
public interface RoleResourceMapper extends BaseMapper<RoleResource> {
    /**
     * 返回指定角色所有权限,对应的在中间表中的主键
     * 使用场景 : 需要先获取对象然后根据 主键删除中间表数据!!
     *
     * @param roleId
     * @return
     */
    @Select("SELECT e.id AS id FROM role r LEFT JOIN role_resource e ON r.id = e.role_id WHERE r.id = #{id}")
    Long selectIdListByRoleId(@Param("id") Long roleId);

    /**
     * 根据资源 id 从中间表中删除数据
     * 禁止对某一个资源的进行访问
     *
     * @param resourceId
     * @return
     */
    @Delete("DELETE FROM role_resource WHERE resource_id = #{resourceId}")
    int deleteByResourceId(@Param("resourceId") Serializable resourceId);

}