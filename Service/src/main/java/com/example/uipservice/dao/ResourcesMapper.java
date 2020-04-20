package com.example.uipservice.dao;

import com.example.uipservice.entity.Resources;

public interface ResourcesMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(Resources record);

    int insertSelective(Resources record);

    Resources selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(Resources record);

    int updateByPrimaryKey(Resources record);
}