package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.UniversityMapper;
import com.example.uipservice.entity.University;
import com.example.uipservice.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class UniversityServiceImpl implements UniversityService {

    @Autowired
    UniversityMapper universityMapper;

    @Override
    public boolean insertUniversity(University university) {
        if(university.getUniversityName() != null){
            try{
                int effectNum = universityMapper.insert(university);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，插入大学信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("插入的大学名为空！");
        }
    }

    @Override
    public boolean updateUniversity(University university) {
        if(university.getUniversityName() != null){
            try{
                int effectNum = universityMapper.updateByPrimaryKey(university);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，更新大学信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("插入的大学名为空！");
        }
    }

    @Override
    public boolean deleteUniversity(Integer infoId) {
        if(infoId != null){
            try{
                int effectNum = universityMapper.deleteByPrimaryKey(infoId);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，删除大学信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("删除的大学信息的id为空！");
        }
    }

    @Override
    public Map queryUniversity() {
        Map universityListMap = new HashMap();

        try{
            List<University> list = universityMapper.queryUniversity();
            universityListMap.put("universityList",list);
            return universityListMap;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
