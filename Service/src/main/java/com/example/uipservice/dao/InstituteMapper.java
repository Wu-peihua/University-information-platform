package com.example.uipservice.dao;

import com.example.uipservice.entity.Institute;

public interface InstituteMapper {
    int deleteByPrimaryKey(Integer infoId);

    int insert(Institute record);

    int insertSelective(Institute record);

    Institute selectByPrimaryKey(Integer infoId);

    int updateByPrimaryKeySelective(Institute record);

    int updateByPrimaryKey(Institute record);
}