package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.InstituteMapper;
import com.example.uipservice.dao.StuCertificationMapper;
import com.example.uipservice.dao.UniversityMapper;
import com.example.uipservice.dao.UserInfoMapper;
import com.example.uipservice.entity.Recruit;
import com.example.uipservice.entity.StuCertification;
import com.example.uipservice.service.CertificationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CertificationImpl implements CertificationService {

    @Autowired
    StuCertificationMapper certificationMapper;
    @Autowired
    UniversityMapper universityMapper;
    @Autowired
    InstituteMapper instituteMapper;
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public boolean insertCertification(StuCertification certification) {
        try {
            int effectNum = certificationMapper.insertSelective(certification);
            if (effectNum > 0) return true;
            else throw new RuntimeException("服务器错误！插入认证信息失败！");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean updateCertification(StuCertification certification) {
        try {
            int effectNum = certificationMapper.updateByPrimaryKeySelective(certification);
            if (effectNum > 0) return true;
            else throw new RuntimeException("服务器错误！修改认证信息失败！");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean certificationPass(Long infoId) {
        if(infoId != null){
            try {
                StuCertification stuCertification = certificationMapper.selectByPrimaryKey(infoId);
                int effect = userInfoMapper.updateUserToStudent(stuCertification.getStuNumber(),stuCertification.getStuCard(),stuCertification.getUniversityId(),stuCertification.getInstitudeId(),stuCertification.getUserId());
                int effectNum = certificationMapper.certificationPass(infoId);
                if(effectNum > 0 && effect > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，学生认证失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else {
            throw new RuntimeException("修改学生认证申请的id为空！");
        }
    }

    @Override
    public boolean certificationUnPass(Long infoId) {
        if(infoId != null){
            try {
                int effectNum = certificationMapper.certificationUnPass(infoId);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，学生认证失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else {
            throw new RuntimeException("修改学生认证申请的id为空！");
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

    @Override
    public Map queryStuCertification(Integer pageNum, Integer pageSize) {
        Map certificationMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<StuCertification> data = certificationMapper.queryStuCertification();
        List<String> universityList = new ArrayList<>();
        for(StuCertification stuCertification: data){
            universityList.add(universityMapper.selectByPrimaryKey(stuCertification.getUniversityId()).getUniversityName());
        }
        List<String> instituteList = new ArrayList<>();
        for(StuCertification stuCertification: data){
            instituteList.add(instituteMapper.selectByPrimaryKey(stuCertification.getInstitudeId()).getInstituteName());
        }
        certificationMap.put("universityList",universityList);
        certificationMap.put("instituteList",instituteList);
        certificationMap.put("certificationList",data);  //分页获取的数据
        certificationMap.put("total",data.getTotal());       //总页数
        certificationMap.put("pageSize",data.getPageSize());     //每页大小
        certificationMap.put("pageNum",pageNum);     //页数
        return certificationMap;
    }

    @Override
    public Map queryStuCertificationByUniAndIns(Integer pageNum, Integer pageSize, Integer universityId, Integer instituteId) {
        Map certificationMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<StuCertification> data = certificationMapper.queryStuCertificationByUniAndIns(universityId, instituteId);
        List<String> universityList = new ArrayList<>();
        for(StuCertification stuCertification: data){
            universityList.add(universityMapper.selectByPrimaryKey(stuCertification.getUniversityId()).getUniversityName());
        }
        List<String> instituteList = new ArrayList<>();
        for(StuCertification stuCertification: data){
            instituteList.add(instituteMapper.selectByPrimaryKey(stuCertification.getInstitudeId()).getInstituteName());
        }
        certificationMap.put("universityList",universityList);
        certificationMap.put("instituteList",instituteList);
        certificationMap.put("certificationList",data);  //分页获取的数据
        certificationMap.put("total",data.getTotal());       //总页数
        certificationMap.put("pageSize",data.getPageSize());     //每页大小
        certificationMap.put("pageNum",pageNum);     //页数
        return certificationMap;
    }
}
