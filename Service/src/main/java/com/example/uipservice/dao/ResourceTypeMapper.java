package com.example.uipservice.dao;

import com.example.uipservice.entity.ResourceType;

public interface ResourceTypeMapper {
    int deleteByPrimaryKey(Integer infoId);

    int insert(ResourceType record);

    int insertSelective(ResourceType record);

    ResourceType selectByPrimaryKey(Integer infoId);

    int updateByPrimaryKeySelective(ResourceType record);

    int updateByPrimaryKey(ResourceType record);
}