package com.fmi110.controller;

import com.fmi110.model.Resource;
import com.fmi110.service.IResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;

/**
 * 资源管理
 * 1 跳转管理页面
 * 2 新增资源
 * 3 删除资源
 * 4 更新资源
 * 5 获取所有资源(Tree 菜单)
 * 6 获取所有资源,dataGrid 网格
 */
@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController {
    @Autowired
    private IResourceService resourceService;

    /**
     * 跳转管理页面
     * @return
     */
    @GetMapping("/manager")
    public Object manager(){
        return "admin/resource/resource";
    }

    /**
     * 当前用户的菜单树 , 前端树形菜单用的
     * @return
     */
    @RequestMapping(value="/tree")
    @ResponseBody
    public Object tree(){
        return resourceService.selectTree(super.getLogginUser());
    }

    /**
     * 前端数据网格控件使用的数据  easyui 的 dataGrid
     * @return
     */
    @RequestMapping("/treeGrid")
    @ResponseBody
    public Object treeGrid(){
        return resourceService.selectAll();
    }

    /**
     * 跳转添加页面
     * @return
     */
    @RequestMapping("/addPage")
    public String addPage(){
        return "admin/resource/resourceAdd";
    }

    /**
     * 添加资源
     * @param resource
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public Object add(@Valid Resource resource) {
        resource.setCreateTime(new Date());
        resourceService.insert(resource);
        return getResultMap("200", "添加成功");
    }


    /**
     * 查询所有的菜单
     */
    @RequestMapping("/allTree")
    @ResponseBody
    public Object allMenu(){
        return this.resourceService.selectAllTree();
    }

    /**
     * 跳转编辑页面
     * @param model
     * @param id
     * @return
     */
    @RequestMapping("/editPage")
    public String editPage(Model model, Long id) {
        Resource resource = resourceService.selectById(id);
        model.addAttribute("resource", resource);
        return "admin/resource/resourceEdit";
    }

    /**
     * 更新资源信息
     * @param resource
     * @return
     */
    @RequestMapping("/edit")
    @ResponseBody
    public Object edit(@Valid Resource resource) {
        resourceService.updateById(resource);
        return getResultMap("200", "更新成功");
    }

    /**
     * 根据 id 删除资源
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Long id) {
        resourceService.deleteById(id);
        return getResultMap("200", "删除成功");
    }
}
