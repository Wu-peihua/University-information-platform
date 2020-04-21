package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.InstituteMapper;
import com.example.uipservice.entity.Institute;
import com.example.uipservice.service.InstituteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class InstituteServiceImpl implements InstituteService {

    @Autowired
    private InstituteMapper instituteMapper;

    @Override
    public boolean insertInstitute(Institute institute) {
        if(institute.getInstituteName() != null){
            try{
                int effectNum = instituteMapper.insert(institute);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，插入学院信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("插入学院的名为空！");
        }
    }

    @Override
    public boolean updateInstitute(Institute institute) {
        if(institute.getInstituteName() != null){
            try{
                int effectNum = instituteMapper.updateByPrimaryKey(institute);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，更新学院信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("插入的学院名为空！");
        }
    }

    @Override
    public boolean deleteInstitute(Integer infoId) {
        if(infoId != null){
            try{
                int effectNum = instituteMapper.deleteByPrimaryKey(infoId);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，删除学院信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("删除的学院信息的id为空！");
        }       }

    @Override
    public Map queryInstitute() {
        Map instituteListMap = new HashMap();

        try{
            List<Institute> list = instituteMapper.queryInstitute();
            instituteListMap.put("instituteList",list);
            return instituteListMap;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }    }
}
