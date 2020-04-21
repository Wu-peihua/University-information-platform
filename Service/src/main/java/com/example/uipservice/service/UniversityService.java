package com.example.uipservice.service;

import com.example.uipservice.entity.Recruit;
import com.example.uipservice.entity.University;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UniversityService {

    //插入一条大学信息
    boolean insertUniversity(University university);

    //根据id更新大学信息
    boolean updateUniversity(University university);

    //根据id删除大学信息
    boolean deleteUniversity(Integer infoId);

    /*分页获取所有的大学的信息*/
    Map queryUniversity();

}
