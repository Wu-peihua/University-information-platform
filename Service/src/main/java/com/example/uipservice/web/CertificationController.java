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
        modelMap.put("success", certificationService.insertCertification(certification));
        return modelMap;
    }

    /**
     * 获取认证信息
     * @return modelMap
     */
    @RequestMapping(value = "/selectuncheckcertification", method = RequestMethod.GET)
    private Map selectCertification(Long userId) {
        return certificationService.selectUncheckCertification(userId);
    }
}
