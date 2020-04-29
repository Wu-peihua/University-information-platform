package com.example.uipservice.web;

import com.example.uipservice.entity.CourseEvaluation;
import com.example.uipservice.service.CourseEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/courseeval")
public class CourseEvaluationController {

    @Autowired
    CourseEvaluationService courseEvaluationService;

    /**
     * 插入一条课程评论信息
     * @return modelMap
     */
    @RequestMapping(value = "/insertcourseeval", method = RequestMethod.POST)
    private Map<String, Object> insertCourseEvaluation(@RequestBody CourseEvaluation courseEvaluation){
//        System.out.println("insert recruit: " + recruit);
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", courseEvaluationService.insertCourseEvaluation(courseEvaluation));
        return modelMap;
    }


    /**
     * 更新课程评论信息
     * @return modelMap
     */
    @RequestMapping(value = "/updatecourseeval", method = RequestMethod.POST)
    private Map<String,Object> updateCourseEvaluation(CourseEvaluation courseEvaluation){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", courseEvaluationService.updateCourseEvaluation(courseEvaluation));
        return modelMap;
    }

    /**
     * 删除一条评论信息
     * @return modelMap
     */
    @RequestMapping(value = "/deletecourseeval", method = RequestMethod.POST)
    private Map<String,Object> deleteCourseEvaluation(Long infoId){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", courseEvaluationService.deleteCourseEvaluation(infoId));
        return modelMap;
    }


    /**
     * 根据用户id分页获取评论信息
     * @return modelMap
     */
    @RequestMapping(value = "/querycourseevalbyuserid", method = RequestMethod.GET)
    private Map queryCourseEvaluationByUserId(Integer pageNum, Integer pageSize, Long userId){
        return courseEvaluationService.queryCourseEvaluationByUserId(pageNum, pageSize, userId);
    }

    /**
     * 根据信息主键id分页获取评论信息
     * @return modelMap
     */
    @RequestMapping(value = "/querycourseevalbyinfoid", method = RequestMethod.GET)
    private Map queryCourseEvaluationByInfoId(Integer pageNum, Integer pageSize, Long infoId){
        return courseEvaluationService.queryCourseEvaluationByInfoId(pageNum, pageSize, infoId);
    }


    /**
     * 分页获取所有评论信息
     * @return modelMap
     */
    @RequestMapping(value = "/querycourseeval", method = RequestMethod.GET)
    private Map queryCourseEvaluation(Integer pageNum, Integer pageSize){
        return courseEvaluationService.queryCourseEvaluation(pageNum, pageSize);
    }

    /**
     * 根据学校ID和学院ID分页获取所有评论信息
     * @return modelMap
     */
    @RequestMapping(value = "/querycourseevalbycourseid", method = RequestMethod.GET)
    private Map queryCourseEvaluationByCourseId(Integer pageNum, Integer pageSize, Long courseId){
        return courseEvaluationService.queryCourseEvaluationByCourseId(pageNum,pageSize,courseId);
    }





}
