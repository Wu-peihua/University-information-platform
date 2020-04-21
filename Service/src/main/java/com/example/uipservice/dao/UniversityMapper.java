package com.example.uipservice.dao;

import com.example.uipservice.entity.University;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UniversityMapper {
    int deleteByPrimaryKey(Integer infoId);

    int insert(University record);

    int insertSelective(University record);

    University selectByPrimaryKey(Integer infoId);

    int updateByPrimaryKeySelective(University record);

    int updateByPrimaryKey(University record);

    List<University> queryUniversity();
}