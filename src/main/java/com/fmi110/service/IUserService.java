package com.fmi110.service;

import com.baomidou.mybatisplus.service.IService;
import com.fmi110.commons.result.PageBean;
import com.fmi110.model.User;
import com.fmi110.model.vo.UserVo;

import java.util.List;

public interface IUserService extends IService<User> {
    List<User> selectByLoginName(UserVo userVo);

    UserVo selectVoById(Long id);

    /**
     * 带条件筛选的分页查找
     * @param pageBean
     * @return
     */
    PageBean selectDataGrid(PageBean pageBean);

    void insertByVo(UserVo userVo);

    void updateByVo(UserVo userVo);
    /**
     * 更新密码
     * @param userId
     * @param md5Hex 加密后的密码
     */
    void updatePwdByUserId(Long userId, String md5Hex);

    void deleteUserById(Long id);

}
