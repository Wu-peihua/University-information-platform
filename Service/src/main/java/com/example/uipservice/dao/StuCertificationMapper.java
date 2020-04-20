package com.example.uipservice.dao;

import com.example.uipservice.entity.StuCertification;

public interface StuCertificationMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(StuCertification record);

    int insertSelective(StuCertification record);

    StuCertification selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(StuCertification record);

    int updateByPrimaryKey(StuCertification record);
}