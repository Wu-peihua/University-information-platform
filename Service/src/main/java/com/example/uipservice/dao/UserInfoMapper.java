package com.example.uipservice.dao;

import com.example.uipservice.entity.UserInfo;
import org.springframework.stereotype.Service;

@Service
public interface UserInfoMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Long userId);

    UserInfo selectByUserName(String name);

    UserInfo getUserInfoByUserPassword(String userName, String userPassword);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);


}