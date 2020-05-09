package com.example.uipservice.dao;

import com.example.uipservice.entity.StuCertification;
import org.springframework.stereotype.Service;

@Service
public interface StuCertificationMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(StuCertification record);

    int insertSelective(StuCertification record);

    StuCertification selectUncheck(Long userId);

    StuCertification selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(StuCertification record);

    int updateByPrimaryKey(StuCertification record);
}