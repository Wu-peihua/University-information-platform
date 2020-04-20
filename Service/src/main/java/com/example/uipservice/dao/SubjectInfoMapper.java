package com.example.uipservice.dao;

import com.example.uipservice.entity.SubjectInfo;

public interface SubjectInfoMapper {
    int deleteByPrimaryKey(Integer infoId);

    int insert(SubjectInfo record);

    int insertSelective(SubjectInfo record);

    SubjectInfo selectByPrimaryKey(Integer infoId);

    int updateByPrimaryKeySelective(SubjectInfo record);

    int updateByPrimaryKey(SubjectInfo record);
}