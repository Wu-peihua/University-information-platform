package com.example.uipservice.service;

import com.example.uipservice.entity.StuCertification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CertificationService {

    // 新建一条认证
    boolean insertCertification(StuCertification certification);

    //根据id修改stu_cer，认证成功
    boolean certificationPass(Long infoId);

    //根据id修改stu_cer，认证失败
    boolean certificationUnPass(Long infoId);

    // 获取认证信息
    Map selectCertificationByUserId(Long userId);

    /*显示所有学生申请信息*/
    Map queryStuCertification(Integer pageNum, Integer pageSize);

    /*根据学校学院id显示所有学生申请信息*/
    Map queryStuCertificationByUniAndIns(Integer pageNum, Integer pageSize, Integer universityId,Integer instituteId);

}
