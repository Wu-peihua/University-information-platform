package com.example.uipservice.dao;

import com.example.uipservice.entity.Recruit;

public interface RecruitMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(Recruit record);

    int insertSelective(Recruit record);

    Recruit selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(Recruit record);

    int updateByPrimaryKey(Recruit record);
}