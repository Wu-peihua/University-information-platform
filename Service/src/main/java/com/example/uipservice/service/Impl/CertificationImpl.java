package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.StuCertificationMapper;
import com.example.uipservice.entity.StuCertification;
import com.example.uipservice.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class CertificationImpl implements CertificationService {

    @Autowired
    StuCertificationMapper certificationMapper;

    @Override
    public boolean insertCertification(StuCertification certification) {
        try {
            int effectNum = certificationMapper.insert(certification);
            if (effectNum > 0) return true;
            else throw new RuntimeException("服务器错误！插入认证信息失败！");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map selectCertificationByUserId(Long userId) {
        Map resMap = new HashMap();
        try {
            StuCertification certification = certificationMapper.selectByUserId(userId);
            resMap.put("certification", certification);
            return resMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
