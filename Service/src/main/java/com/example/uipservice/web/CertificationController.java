package com.example.uipservice.web;

import com.example.uipservice.entity.StuCertification;
import com.example.uipservice.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/certification")
public class CertificationController {

    @Autowired
    CertificationService certificationService;

    /**
     * 插入新认证信息
     * @return modelMap
     */
    @RequestMapping(value = "/insertcertification", method = RequestMethod.POST)
    private Map<String, Object> insertCertification(@RequestBody StuCertification certification) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("result", certificationService.insertCertification(certification));
        return modelMap;
    }

    /**
     * 获取认证信息
     * @return modelMap
     */
    @RequestMapping(value = "/selectcertificationbyuserid", method = RequestMethod.GET)
    private Map selectCertification(Long userId) {
        return certificationService.selectCertificationByUserId(userId);
    }

    /**
     * 根据学校学院id分页获取所有认证信息
     * @return modelMap
     */
    @RequestMapping(value = "/querycertificationbyuniandins", method = RequestMethod.GET)
    private Map queryStuCertificationByUniAndIns(Integer pageNum, Integer pageSize, Integer universityId, Integer instituteId){
        return certificationService.queryStuCertificationByUniAndIns(pageNum,pageSize,universityId,instituteId);
    }

    /**
     * 分页获取所有认证信息
     * @return modelMap
     */
    @RequestMapping(value = "/querycertification", method = RequestMethod.GET)
    private Map queryStuCertification(Integer pageNum, Integer pageSize){
        return certificationService.queryStuCertification(pageNum,pageSize);
    }

    /**
     * 认证成功
     * @return modelMap
     */
    @RequestMapping(value = "/certificationpass", method = RequestMethod.POST)
    private Map<String,Object> certificationPass(Long infoId){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", certificationService.certificationPass(infoId));
        return modelMap;
    }
    /**
     * 认证失败
     * @return modelMap
     */
    @RequestMapping(value = "/certificationunpass", method = RequestMethod.POST)
    private Map<String,Object> certificationUnPass(Long infoId){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", certificationService.certificationUnPass(infoId));
        return modelMap;
    }
}
