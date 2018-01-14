package com.fmi110.controller;

import com.fmi110.commons.result.PageBean;
import com.fmi110.commons.shiro.PasswordHash;
import com.fmi110.commons.utils.StringUtils;
import com.fmi110.model.Role;
import com.fmi110.model.User;
import com.fmi110.model.vo.UserVo;
import com.fmi110.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户管理
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;
    @Autowired
    private PasswordHash passwordHash;
    /**
     * 用户管理页
     *
     * @return
     */
    @GetMapping("/manager")
    public String manager() {
        return "admin/user/user";
    }

    /**
     * 用户管理列表 dataGrid
     * @param userVo
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @PostMapping("/dataGrid")
    @ResponseBody
    public Object dataGrid(UserVo userVo,Integer page,Integer rows,String sort,String order) {
        PageBean pageBean = new PageBean(page, rows, sort, order);
        Map<String, Object> condition = new HashMap<>();
        if (StringUtils.isBlank(userVo.getName())) {
            condition.put("name", userVo.getName());
        }

        if (userVo.getOrganizationId() != null) {
            condition.put("organizationeId", userVo.getOrganizationId());
        }

        if (userVo.getCreatedateStart() != null) {
            condition.put("startTime", userVo.getCreatedateStart());
        }
        if (userVo.getCreatedateEnd() != null) {
            condition.put("endTime", userVo.getCreatedateEnd());
        }
        pageBean.setCondition(condition);
        return userService.selectDataGrid(pageBean);
    }

    /**
     * 添加用户页
     *
     * @return
     */
    @GetMapping("/addPage")
    public String addPage() {
        return "admin/user/userAdd";
    }

    /**
     * 添加用户
     *
     * @param userVo
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public Object add(@Valid UserVo userVo) {
        List<User> list = this.userService.selectByLoginName(userVo);
        if (list != null && list.size() > 0) {
            return super.getResultMap("error", "用户名已存在");
        }
        // 盐
        String salt = StringUtils.getUUId();
        String pwd = passwordHash.toHex(userVo.getPassword(), salt);
        userVo.setPassword(salt);
        userVo.setPassword(pwd);
        this.userService.insertByVo(userVo);
        return super.getResultMap("200", "添加用户成功");
    }
    /**
     * 编辑用户页,
     * 用户信息存放到request域 key 为 roleIds , user
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/editPage")
    public String editPage(Model model, Long id) {
        UserVo userVo = userService.selectVoById(id);
        List<Role> rolesList = userVo.getRolesList();
        List<Long> ids = new ArrayList<>();
        for (Role role : rolesList) {
            ids.add(role.getId());
        }
        model.addAttribute("roleIds", ids);
        model.addAttribute("user", userVo);
        return "admin/user/userEdit";
    }

    /**
     * 编辑用户
     *
     * @param userVo
     * @return
     */
    @RequiresRoles("admin")
    @PostMapping("/edit")
    @ResponseBody
    public Object edit(@Valid UserVo userVo) {
        List<User> list = userService.selectByLoginName(userVo);
        if (null != list && list.size() > 0) {
            return super.getResultMap("error", "用户名已存在");
        }
        // 更新密码应该在专门的链接修改,这里不改所以置空
        userVo.setPassword(null);
        userService.updateByVo(userVo);
        return super.getResultMap("200", "修改成功");
    }
    /**
     * 修改密码页
     *
     * @return
     */
    @GetMapping("/editPwdPage")
    public String editPwdPage() {
        return "admin/user/userEditPwd";
    }

    /**
     * 修改密码
     *
     * @param oldPwd
     * @param pwd
     * @return
     */
    @PostMapping("/editUserPwd")
    @ResponseBody
    public Object editUserPwd(String oldPwd, String pwd) {
        Long userId = super.getLogginUserId();
        User user         = userService.selectById(userId);
        // 拿盐
        String salt = user.getSalt();
        if (!user.getPassword()
                 .equals(passwordHash.toHex(oldPwd, salt))) {
            return super.getResultMap("error", "旧密码输入错误");
        }
        userService.updatePwdByUserId(userId,passwordHash.toHex(pwd,salt));
        return super.getResultMap("200", "密码修改成功");
    }
    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @RequiresRoles("admin")
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(Long id) {
        Long currentUserId = super.getLogginUserId();
        if (id == currentUserId) {
            return super.getResultMap("error", "不可以删除自己");
        }
        userService.deleteById(id);
        return super.getResultMap("200", "删除用户成功");
    }
}
