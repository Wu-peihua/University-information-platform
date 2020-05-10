package com.example.uipservice.service;

import com.example.uipservice.entity.StuCertification;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CertificationService {

    // 新建一条认证
    boolean insertCertification(StuCertification certification);

    // 获取认证信息
    Map selectCertificationByUserId(Long userId);
}
