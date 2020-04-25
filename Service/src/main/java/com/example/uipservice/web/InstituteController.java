package com.example.uipservice.web;


import com.example.uipservice.entity.Institute;
import com.example.uipservice.entity.Recruit;
import com.example.uipservice.service.InstituteService;
import com.example.uipservice.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/institute")
public class InstituteController {

    @Autowired
    InstituteService instituteService;

    /**
     * 插入学院信息
     * @return modelMap
     */
    @RequestMapping(value = "/insertinstitute", method = RequestMethod.POST)
    private Map<String, Object> insertInstitute(@RequestBody Institute institute){
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", instituteService.insertInstitute(institute));
        return modelMap;
    }

    /**
     * 更新学院信息
     * @return modelMap
     */
    @RequestMapping(value = "/updateinstitute", method = RequestMethod.POST)
    private Map<String,Object> updateInstitute(Institute institute){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", instituteService.updateInstitute(institute));
        return modelMap;
    }

    /**
     * 删除学院信息
     * @return modelMap
     */
    @RequestMapping(value = "/deleteinstitute", method = RequestMethod.POST)
    private Map<String,Object> deleteInstitute(Integer infoId){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", instituteService.deleteInstitute(infoId));
        return modelMap;
    }

    /**
     * 获取所有学院信息
     * @return modelMap
     */
    @RequestMapping(value = "/queryinstitute", method = RequestMethod.GET)
    private Map queryInstitute(){

        return instituteService.queryInstitute();
    }




}
