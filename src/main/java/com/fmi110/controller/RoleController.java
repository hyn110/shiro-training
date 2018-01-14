package com.fmi110.controller;

import com.fmi110.commons.result.PageBean;
import com.fmi110.model.Role;
import com.fmi110.service.IRoleService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色管理 , 也是权利权限管理
 * 通过赋予角色特定的权限实现权限管理
 * 
 * 1 跳转角色页面
 * 2 新增角色
 * 3 删除角色
 * 4 更新角色--权限
 * 5 获取所有角色(Tree 菜单)
 * 6 获取所有角色,dataGrid 网格
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController{
    @Autowired
    private IRoleService roleService;

    /**
     * 跳转权限(角色)角色页面
     * @return
     */
    @GetMapping("/manager")
    public String manager(){
        return "admin/role/role";
    }

    /**
     * 权限列表,分页
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    public Object dataGrid(Integer page, Integer rows, String sort, String order) {
        PageBean pageBean = new PageBean(page, rows, sort, order);
        return roleService.selectDataGrid(pageBean);
    }

    /**
     * 返回所有的角色
     * @return
     */
    @PostMapping("/tree")
    @ResponseBody
    public Object tree(){
        return this.roleService.selectTree();
    }

    /**
     * 添加权限
     *
     * @param role
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid Role role) {
        roleService.insert(role);
        return super.getResultMap("200", "添加成功");
    }

    /**
     * 删除权限
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Long id) {
        roleService.deleteById(id);
        return super.getResultMap("200", "删除权限成功");
    }

    /**
     * 编辑权限页
     *
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(Model model, Long id) {
        Role role = roleService.selectById(id);
        model.addAttribute("role", role); // 加入到 request 域
        return "admin/role/roleEdit";
    }

    /**
     * 编辑权限
     *
     * @param role
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Role role) {
        roleService.updateById(role);
        return super.getResultMap("200", "编辑权限成功");
    }

    /**
     * 授权页面
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/grantPage")
    public String grantPage(Model model, Long id) {
        model.addAttribute("id", id);
        return "admin/role/roleGrant";
    }

    /**
     * 授权页面根据角色 id 查询资源(id)
     * @param id
     * @return
     */
    @RequestMapping("/findResourceIdListByRoleId")
    @ResponseBody
    public Object findResourceByRoleId(Long id) {
        List<Long> rosourceId = this.roleService.selectResourceIdListByRoleId(id);
        return super.getResultMap("200", rosourceId);
    }
    /**
     * 授权
     *
     * @param id
     * @param resourceIds
     * @return
     */
    @RequiresRoles("admin")
    @RequestMapping("/grant")
    @ResponseBody
    public Object grant(Long id, String resourceIds) {
        this.roleService.updateRoleResource(id,resourceIds);
        return super.getResultMap("200", "授权成功");
    }
}
