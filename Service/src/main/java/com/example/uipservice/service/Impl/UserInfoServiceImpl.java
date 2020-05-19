package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.UserInfoMapper;
import com.example.uipservice.entity.UserInfo;
import com.example.uipservice.service.UserInfoService;
import com.example.uipservice.util.RSAUtil;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;


@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {


    @Autowired
    UserInfoMapper userInfoMapper;

    @Value("${privateKey}")
    String privateKey;


    @Override
    public boolean insertUserInfo(UserInfo userInfo) {
        if(userInfo.getUserName()!=null && userInfo.getPw()!=null ){
            try{
                UserInfo userInfo1 = userInfoMapper.selectByUserName(userInfo.getUserName());
                if(userInfo1 != null){
                    throw new RuntimeException("用户名已被使用，注册用户失败！");
                }
                userInfo.setPortrait("https://tse1-mm.cn.bing.net/th/id/OIP.kbhcK_jmmGTIrDmD_5ZaKwHaHa?w=207&h=203&c=7&o=5&pid=1.7");
                int effectNum = userInfoMapper.insertSelective(userInfo);
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
        }
    }

    @Override
    public Map<String, Object> login(String userName, String pwd) {
        Map<String,Object> modelMap = new HashMap<>();
        //根据用户名获取用户信息
        UserInfo userInfo = userInfoMapper.selectByUserName(userName);
        if(userInfo != null){
            try {
                //解密pwd
                RSAPrivateKey key = null;
                key = RSAUtil.getPrivateKey(privateKey);
                String resultKey = RSAUtil.privateDecrypt(pwd,key);
                //与数据库中的密码对比
                if(resultKey.equals(userInfo.getPw())){
                    modelMap.put("success",true);
                    modelMap.put("userInfo",userInfo);
                }else{
                    //密码错误信息
                    System.out.println("密码错误！");
                    modelMap.put("success",false);
                    modelMap.put("msg","密码错误！");
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("用户名不存在！");
            modelMap.put("success",false);
            modelMap.put("msg","用户名不存在！");
        }
        return  modelMap;
    }

    @Override
    public Map<String, Object> regist(String userName, String pwd) {
        Map<String,Object> modelMap = new HashMap<>();
        UserInfo userInfo = userInfoMapper.selectByUserName(userName);
        if(userInfo == null){
            //用户名未被使用，插入用户
            RSAPrivateKey key = null;
            try {
                //解密pwd
                key = RSAUtil.getPrivateKey(privateKey);
                String resultKey = RSAUtil.privateDecrypt(pwd,key);

                userInfo = new UserInfo();
                userInfo.setUserName(userName);
                userInfo.setPw(resultKey);
                int effectNum = userInfoMapper.insertSelective(userInfo);
                userInfo = userInfoMapper.selectByUserName(userName);
                if(effectNum > 0 ){
                    //增加成功
                    modelMap.put("success",true);
                    modelMap.put("userInfo",userInfo);
                }else{
                    System.out.println("注册失败");
                    modelMap.put("success",false);
                    modelMap.put("msg","注册失败！");
                }

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }

        }else{
            System.out.println("用户名已存在");
            modelMap.put("success",false);
            modelMap.put("msg","用户名已存在");
        }
        return modelMap;
    }


}
