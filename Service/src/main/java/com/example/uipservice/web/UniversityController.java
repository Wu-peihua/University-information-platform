package com.example.uipservice.web;


import com.example.uipservice.entity.Institute;
import com.example.uipservice.entity.University;
import com.example.uipservice.service.InstituteService;
import com.example.uipservice.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/university")
public class UniversityController {

    @Autowired
    UniversityService universityService;

    /**
     * 插入大学信息
     * @return modelMap
     */
    @RequestMapping(value = "/insertuniversity", method = RequestMethod.POST)
    private Map<String, Object> insertUniversity(@RequestBody University university){
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", universityService.insertUniversity(university));
        return modelMap;
    }

    /**
     * 更新大学信息
     * @return modelMap
     */
    @RequestMapping(value = "/updateuniversity", method = RequestMethod.POST)
    private Map<String,Object> updateUniversity(University university){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", universityService.updateUniversity(university));
        return modelMap;
    }

    /**
     * 删除大学信息
     * @return modelMap
     */
    @RequestMapping(value = "/deleteUniversity", method = RequestMethod.POST)
    private Map<String,Object> deleteUniversity(Integer infoId){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", universityService.deleteUniversity(infoId));
        return modelMap;
    }

    /**
     * 获取所有大学信息
     * @return modelMap
     */
    @RequestMapping(value = "/queryuniversity", method = RequestMethod.GET)
    private Map<String,Object> queryUniversity(){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", universityService.queryUniversity());
        return modelMap;
    }


}
