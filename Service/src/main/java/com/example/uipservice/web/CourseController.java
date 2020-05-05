package com.example.uipservice.web;


import com.example.uipservice.entity.Courses;
import com.example.uipservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    CourseService courseService;

    /**
     * 插入课程信息
     * @return modelMap
     */
    @RequestMapping(value = "/insertcourse", method = RequestMethod.POST)
    private Map<String, Object> insertCourse(@RequestBody Courses Course){
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", courseService.insertCourse(Course));
        return modelMap;
    }

    /**
     * 更新课程信息
     * @return modelMap
     */
    @RequestMapping(value = "/updatecourse", method = RequestMethod.POST)
    private Map<String,Object> updateCourse(Courses Course){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", courseService.updateCourse(Course));
        return modelMap;
    }

    /**
     * 删除课程信息
     * @return modelMap
     */
    @RequestMapping(value = "/deletecourse", method = RequestMethod.POST)
    private Map<String,Object> deleteCourse(Long infoId){
        Map<String,Object> modelMap = new HashMap<String, Object>();
        modelMap.put("success", courseService.deleteCourse(infoId));
        return modelMap;
    }

    /**
     * 根据信息主键id分页获取组队信息
     * @return modelMap
     */
    @RequestMapping(value = "/querycoursebyinfoid", method = RequestMethod.GET)
    private Map queryCourseByInfoId(Integer pageNum, Integer pageSize, Long infoId){
        return courseService.queryCourseByInfoId(pageNum, pageSize, infoId);
    }

    /**
     * 根据学校ID和学院ID分页获取所有组队信息
     * @return modelMap
     */
    @RequestMapping(value = "/querycoursebyuniandins", method = RequestMethod.GET)
    private Map queryCourseByUniAndIns(Integer pageNum, Integer pageSize, Integer universityId,Integer instituteId){
        return courseService.queryCourseByUniAndIns(pageNum, pageSize, universityId, instituteId);
    }

    /**
     * 分页获取所有组队信息
     * @return modelMap
     */
   
    @RequestMapping(value = "/querycourses", method = RequestMethod.GET)
    private Map queryCourses(Integer pageNum, Integer pageSize){
        return courseService.queryCourses(pageNum, pageSize);

    }


}
