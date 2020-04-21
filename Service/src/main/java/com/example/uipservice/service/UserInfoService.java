package com.example.uipservice.service;

import com.example.uipservice.entity.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface UserInfoService {

    //添加一个新用户
    boolean insertUserInfo(UserInfo userInfo);

    //更新用户信息
    boolean updateUserInfo(UserInfo userInfo);

    //通过用户名获取用户信息
    UserInfo getUserInfoByUserName(String userName);

    //通过Id获取用户信息
    UserInfo getUserInfoByUserId(Long userId);

    //通过用户名和密码获取用户信息
    UserInfo getUserInfoByUserPassword(String userName, String userPassword);

}
