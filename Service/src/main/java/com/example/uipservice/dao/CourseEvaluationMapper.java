package com.example.uipservice.dao;

import com.example.uipservice.entity.CourseEvaluation;

public interface CourseEvaluationMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(CourseEvaluation record);

    int insertSelective(CourseEvaluation record);

    CourseEvaluation selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(CourseEvaluation record);

    int updateByPrimaryKey(CourseEvaluation record);
}