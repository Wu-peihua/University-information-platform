package com.example.uipservice.service;

import com.example.uipservice.entity.CourseEvaluation;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CourseEvaluationService {

    //插入一条课程点评信息
    boolean insertCourseEvaluation(CourseEvaluation courseEvaluation);

    //根据id更新点评信息
    boolean updateCourseEvaluation(CourseEvaluation courseEvaluation);

    //根据id删除一条点评信息
    boolean deleteCourseEvaluation(Long infoId);

    //分页获取所有点评信息
    /*根据评论的用户id分页获取评论信息*/
    Map queryCourseEvaluationByUserId(Integer pageNum, Integer pageSize, Long userId);
    //根据信息主键id分页获取
    Map queryCourseEvaluationByInfoId(Integer pageNum, Integer pageSize, Long infoId);

    /*分页获取所有的课程点评信息*/
    //Map queryCourseEvaluation(Integer pageNum, Integer pageSize);
    //获取课程点评信息列表
    Map queryCourseEvaluation(Integer pageNum, Integer pageSize);
    /*根据评论的courseId分页获取评论信息*/
    Map queryCourseEvaluationByCourseId(Integer pageNum, Integer pageSize,Long courseId);

}
