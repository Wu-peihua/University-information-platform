package com.example.uipservice.service;

import com.example.uipservice.entity.Institute;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface InstituteService {

    //插入学院信息
    boolean insertInstitute(Institute institute);

    //根据id更新学院信息
    boolean updateInstitute(Institute institute);

    //根据id删除学院信息
    boolean deleteInstitute(Integer infoId);

    /*分页获取所有的学院的信息*/
    Map queryInstitute();

}
