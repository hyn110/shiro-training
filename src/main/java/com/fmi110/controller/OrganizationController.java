package com.fmi110.controller;

import com.fmi110.model.Organization;
import com.fmi110.service.IOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;

/**
 * 部门管理
 * 1 跳转管理页面
 * 2 新增部门
 * 3 删除部门
 * 4 更新部门
 * 5 获取所有部门(Tree 菜单)
 * 6 获取所有部门,dataGrid 网格
 */
@Controller
@RequestMapping("/organization")
public class OrganizationController extends BaseController {
    @Autowired
    private IOrganizationService organizationService;

    /**
     * 跳转管理页面
     * @return
     */
    @GetMapping("/manager")
    public Object manager(){
        return "admin/organization/organization";
    }

    /**
     * 部门资源树 , 前端树形菜单用的
     * @return
     */
    @RequestMapping(value="/tree")
    @ResponseBody
    public Object tree(){
        return organizationService.selectTree();
    }

    /**
     * 前端数据网格控件使用的数据  easyui 的 dataGrid
     * @return
     */
    @RequestMapping("/treeGrid")
    @ResponseBody
    public Object treeGrid(){
        return organizationService.selectTreeGrid();
    }

    /**
     * 跳转添加页面
     * @return
     */
    @RequestMapping("/addPage")
    public String addPage(){
        return "admin/organization/organizationAdd";
    }

    /**
     * 添加部门
     * @param organization
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object add(@Valid Organization organization) {
        organization.setCreateTime(new Date());
        organizationService.insert(organization);
        return getResultMap("200", "添加成功");
    }

    /**
     * 跳转编辑页面
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(Model model, Long id) {
        Organization organization = organizationService.selectById(id);
        model.addAttribute("organization", organization);
        return "admin/organization/organizationEdit";
    }

    /**
     * 更新部门信息
     * @param organization
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Organization organization) {
        organizationService.updateById(organization);
        return getResultMap("200", "更新成功");
    }

    /**
     * 根据 id 删除部门
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Long id) {
        organizationService.deleteById(id);
        return getResultMap("200", "删除成功");
    }
}
