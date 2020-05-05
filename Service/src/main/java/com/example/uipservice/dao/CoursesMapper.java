package com.example.uipservice.dao;

import com.example.uipservice.entity.Courses;
import com.example.uipservice.entity.Recruit;
import com.github.pagehelper.Page;

public interface CoursesMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(Courses record);

    int insertSelective(Courses record);

    Courses selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(Courses record);

    int updateByPrimaryKey(Courses record);

    Page<Courses> queryCourses();

    Page<Courses> queryCoursesByUniAndIns(Integer universityId,Integer instituteId);

    Page<Courses> queryCoursesByInfoId(Long infoId);
}