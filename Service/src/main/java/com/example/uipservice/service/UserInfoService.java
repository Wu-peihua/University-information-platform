package com.example.uipservice.service;

import com.example.uipservice.entity.UserInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

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

    /*密码均为加密后的密文传输*/
    //用户登录
    Map<String,Object> login(String userName, String pwd);

    //用户注册
    Map<String,Object> regist(String userName, String pwd);

    // 修改密码
    boolean updatePassword(Long userId, String pw);

}
