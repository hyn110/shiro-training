package com.fmi110.service;

import com.baomidou.mybatisplus.service.IService;
import com.fmi110.model.User;
import com.fmi110.model.vo.UserVo;

import java.util.List;

public interface IUserService extends IService<User> {
    List<User> selectByLoginName(UserVo userVo);

    UserVo selectVoById(Long id);
}
