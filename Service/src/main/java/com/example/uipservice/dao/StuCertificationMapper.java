package com.example.uipservice.dao;

import com.example.uipservice.entity.StuCertification;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import com.github.pagehelper.Page;

@Service
public interface StuCertificationMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(StuCertification record);

    int insertSelective(StuCertification record);

    StuCertification selectByUserId(Long userId);

    StuCertification selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(StuCertification record);

    int updateByPrimaryKey(StuCertification record);

    int certificationPass(Long infoId);

    int certificationUnPass(Long infoId);

    Page<StuCertification> queryStuCertification();

    Page<StuCertification> queryStuCertificationByUniAndIns(@Param("universityId")Integer universityId, @Param("instituteId")Integer instituteId);

}