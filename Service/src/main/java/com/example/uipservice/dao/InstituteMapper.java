package com.example.uipservice.dao;

import com.example.uipservice.entity.Institute;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InstituteMapper {
    int deleteByPrimaryKey(Integer infoId);

    int insert(Institute record);

    int insertSelective(Institute record);

    Institute selectByPrimaryKey(Integer infoId);

    int updateByPrimaryKeySelective(Institute record);

    int updateByPrimaryKey(Institute record);

    List<Institute> queryInstitute();
}