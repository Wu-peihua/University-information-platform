package com.example.uipservice.dao;

import com.example.uipservice.entity.CourseEvaluation;
import com.example.uipservice.entity.ForumComments;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseEvaluationMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(CourseEvaluation record);

    int insertSelective(CourseEvaluation record);

    CourseEvaluation selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(CourseEvaluation record);

    int updateByPrimaryKey(CourseEvaluation record);

    int modifyCommentReportNumberById(Long infoId);

    //Page<CourseEvaluation> queryCourseEvaluation();
    Page<CourseEvaluation> queryCourseEvaluation();

    Page<CourseEvaluation> queryCourseEvaluationByInfoId(Long infoId);

    Page<CourseEvaluation> queryCourseEvaluationByUserId(Long userId);

    Page<CourseEvaluation> queryCourseEvaluationByCourseId(Long courseId);

    Page<CourseEvaluation> queryCourseEvaluationByReport();

    Page<CourseEvaluation> queryCourseEvaluationByUniAndInsByReport(@Param("universityId")Integer universityId, @Param("instituteId")Integer instituteId);



}