package com.example.uipservice.dao;

import com.example.uipservice.entity.Courses;

public interface CoursesMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(Courses record);

    int insertSelective(Courses record);

    Courses selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(Courses record);

    int updateByPrimaryKey(Courses record);
}