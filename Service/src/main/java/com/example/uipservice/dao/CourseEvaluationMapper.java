package com.example.uipservice.dao;

import com.example.uipservice.entity.CourseEvaluation;
import com.github.pagehelper.Page;

public interface CourseEvaluationMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(CourseEvaluation record);

    int insertSelective(CourseEvaluation record);

    CourseEvaluation selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(CourseEvaluation record);

    int updateByPrimaryKey(CourseEvaluation record);

    Page<CourseEvaluation> queryCourseEvaluation();

    Page<CourseEvaluation> queryCourseEvaluationByInfoId(Long infoId);

    Page<CourseEvaluation> queryCourseEvaluationByUserId(Long userId);

    Page<CourseEvaluation> queryCourseEvaluationByCourseId(Long courseId);
}