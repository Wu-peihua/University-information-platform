package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.UserInfoMapper;
import com.example.uipservice.entity.UserInfo;
import com.example.uipservice.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {


    @Autowired
    UserInfoMapper userInfoMapper;


    @Override
    public boolean insertUserInfo(UserInfo userInfo) {
        if(userInfo.getUserName()!=null && userInfo.getPw()!=null ){
            try{
                UserInfo userInfo1 = userInfoMapper.selectByUserName(userInfo.getUserName());
                if(userInfo1 != null){
                    throw new RuntimeException("用户名已被使用，注册用户失败！");
                }
                userInfo.setPortrait("https://tse1-mm.cn.bing.net/th/id/OIP.kbhcK_jmmGTIrDmD_5ZaKwHaHa?w=207&h=203&c=7&o=5&pid=1.7");
                int effectNum = userInfoMapper.insert(userInfo);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，注册用户失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("用户信息不完整！");
        }
    }

    @Override
    public boolean updateUserInfo(UserInfo userInfo) {
        if(userInfo.getUserId()!=null ){
            try{
                UserInfo userInfo1 = userInfoMapper.selectByPrimaryKey(userInfo.getUserId());
                if(userInfo1 != null){
                    throw new RuntimeException("用该用户不存在！");
                }
                int effectNum = userInfoMapper.updateByPrimaryKey(userInfo);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，更新用户信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("用户ID为空！");
        }
    }

    @Override
    public UserInfo getUserInfoByUserName(String userName) {
        if(userName != null){
            try {
                UserInfo userInfo = userInfoMapper.selectByUserName(userName);
                return userInfo;
            }catch (Exception e){
                throw new RuntimeException("服务器操作错误: " + e.getMessage());
            }
        }else {
            throw new RuntimeException("用户名为空！");
        }
    }

    @Override
    public UserInfo getUserInfoByUserId(Long userId) {
        if(userId != null){
            try {
                UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
                return userInfo;
            }catch (Exception e){
                throw new RuntimeException("服务器操作错误: " + e.getMessage());
            }
        }else {
            throw new RuntimeException("用户id为空！");
        }
    }

    @Override
    public UserInfo getUserInfoByUserPassword(String userName, String userPassword) {
        if(userName != null && userPassword != null){
            try {
                UserInfo userInfo = userInfoMapper.getUserInfoByUserPassword(userName,userPassword);
                return userInfo;
            }catch (Exception e){
                throw new RuntimeException("服务器操作错误: " + e.getMessage());
            }
        }else {
            throw new RuntimeException("用户名或密码为空！");
        }    }
}
