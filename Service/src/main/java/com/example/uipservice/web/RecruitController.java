package com.example.uipservice.web;

import com.example.uipservice.entity.Recruit;
import com.example.uipservice.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/recruit")
public class RecruitController {

    @Autowired
    RecruitService recruitService;

    /**
     * 插入一条组队信息
     * @return modelMap
     */
    @RequestMapping(value = "/insertrecruit", method = RequestMethod.POST)
    private Map<String, Object> insertRecruit(@RequestBody Recruit recruit){
//        System.out.println("insert recruit: " + recruit);
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", recruitService.insertRecruit(recruit));
        return modelMap;
    }


    /**
     * 更新组队信息
     * @return modelMap
     */
    @RequestMapping(value = "/updaterecruit", method = RequestMethod.POST)
    private Map<String,Object> updateRecruit(Recruit recruit){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", recruitService.updateRecruit(recruit));
        return modelMap;
    }

    /**
     * 删除一条组队信息
     * @return modelMap
     */
    @RequestMapping(value = "/deleterecruit", method = RequestMethod.POST)
    private Map<String,Object> deleteRecruit(Long infoId){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", recruitService.deleteRecruit(infoId));
        return modelMap;
    }

    /**
     * 根据用户id分页获取组队信息
     * @return modelMap
     */
    @RequestMapping(value = "/queryrecruitbyuserid", method = RequestMethod.GET)
    private Map queryRecruitByUserId(Integer pageNum, Integer pageSize, Long userId){
        return recruitService.queryRecruitByUserId(pageNum, pageSize, userId);
    }

    /**
     * 根据信息主键id分页获取组队信息
     * @return modelMap
     */
    @RequestMapping(value = "/queryrecruitbyinfoid", method = RequestMethod.GET)
    private Map queryRecruitByInfoId(Integer pageNum, Integer pageSize, Long infoId){
        return recruitService.queryRecruitByInfoId(pageNum, pageSize, infoId);
    }

    /**
     * 根据学校ID和学院ID分页获取所有组队信息
     * @return modelMap
     */
    @RequestMapping(value = "/queryrecruitbyuniandins", method = RequestMethod.GET)
    private Map queryRecruitByUniAndIns(Integer pageNum, Integer pageSize, Integer universityId,Integer instituteId){
        return recruitService.queryRecruitByUniAndIns(pageNum, pageSize, universityId,instituteId);
    }

    /**
     * 分页获取所有组队信息
     * @return modelMap
     */
    @RequestMapping(value = "/queryrecruit", method = RequestMethod.GET)
    private Map queryRecruit(Integer pageNum, Integer pageSize){
        return recruitService.queryRecruit(pageNum, pageSize);
    }



}
