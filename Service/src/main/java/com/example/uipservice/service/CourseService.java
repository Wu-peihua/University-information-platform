package com.example.uipservice.service;

import com.example.uipservice.entity.Courses;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CourseService {

    //插入课程信息
    boolean insertCourse(Courses courses);

    //根据id更新课程信息
    boolean updateCourse(Courses courses);

    //根据id删除课程信息
    boolean deleteCourse(Long infoId);

     //分页获取所有课程信息
    /*根据发布人的用户id分页获取组队信息*/
    //Map queryRecruitByUserId(Integer pageNum, Integer pageSize, Long userId);
    //根据信息主键id分页获取
    Map queryCourseByInfoId(Integer pageNum, Integer pageSize, Long infoId);
    /*根据学校ID和学院ID分页获取所有课程信息*/
    Map queryCourseByUniAndIns(Integer pageNum, Integer pageSize, Integer universityId,Integer instituteId);
    /*分页获取所有的课程信息*/
    Map queryCourses(Integer pageNum, Integer pageSize);

}
