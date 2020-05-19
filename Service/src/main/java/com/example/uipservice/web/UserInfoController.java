package com.example.uipservice.web;

import com.example.uipservice.entity.UserInfo;
import com.example.uipservice.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userinfo")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;

    @Value("${publicKey}")
    String publicKey;

    /**
     * 插入一个新用户
     * @return modelMap
     */
    @RequestMapping(value = "/insertuserinfo", method = RequestMethod.POST)
    private Map<String, Object> insertUserInfo(@RequestBody UserInfo userInfo){
        System.out.println("insert userInfo: " + userInfo);
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", userInfoService.insertUserInfo(userInfo));
        return modelMap;
    }


    /**
     * 根据主键查找用户信息
     * @return modelMap
     */
    @RequestMapping(value = "/getuserinfobyuserid", method = RequestMethod.GET)
    private Map<String,Object> getUserInfoByUserId(Long userId){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("userInfo",userInfoService.getUserInfoByUserId(userId));
        return modelMap;
    }

    /**
     * 通过用户名获取用户信息
     * @return modelMap
     */
    @RequestMapping(value = "/getuserinfobyusername", method = RequestMethod.GET)
    private Map<String,Object> getUserInfoByUserName(String userName){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("userInfo",userInfoService.getUserInfoByUserName(userName));
        return modelMap;
    }

    /**
     * 通过用户名和密码获取用户信息
     * @return modelMap
     */
    @RequestMapping(value = "/getuserinfobyusernameandpw", method = RequestMethod.GET)
    private Map<String,Object> getUserInfoByUserNameAndPW(String userName,String pw){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("userInfo",userInfoService.getUserInfoByUserPassword(userName,pw));
        return modelMap;
    }

    /**
     * 更新用户信息
     * @return modelMap
     */
    @RequestMapping(value = "/updateuserinfo", method = RequestMethod.POST)
    private Map<String,Object> updateUserInfo(UserInfo userInfo){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", userInfoService.updateUserInfo(userInfo));
        return modelMap;
    }


    /**
     * 用户登录接口
     * @param pwd 前端发送的RSA加密后的密文
     * @return modelMap
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    private Map<String,Object> login(String userName,String pwd){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        return userInfoService.login(userName,pwd);
    }

    /**
     * 用户注册接口
     * @return modelMap
     */
    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    private Map<String,Object> regist(String userName,String pwd){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        return userInfoService.regist(userName,pwd);
    }

    /**
     * 获取RSA算法公钥
     * @return modelMap
     */
    @RequestMapping(value = "/getpublickey", method = RequestMethod.POST)
    private Map<String,Object> getPublicKey(){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("publicKey",publicKey);
        return modelMap;
    }



}
