package com.example.uipservice.dao;

import com.example.uipservice.entity.University;

public interface UniversityMapper {
    int deleteByPrimaryKey(Integer infoId);

    int insert(University record);

    int insertSelective(University record);

    University selectByPrimaryKey(Integer infoId);

    int updateByPrimaryKeySelective(University record);

    int updateByPrimaryKey(University record);
}