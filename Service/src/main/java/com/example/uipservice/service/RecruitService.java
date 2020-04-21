package com.example.uipservice.service;

import com.example.uipservice.entity.Recruit;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface RecruitService {

    //插入一条组队信息
    boolean insertRecruit(Recruit recruit);

    //根据id更新组队信息
    boolean updateRecruit(Recruit recruit);

    //根据id删除一条组队信息
    boolean deleteRecruit(Long infoId);

    //分页获取所有组队信息
    /*根据发布人的用户id分页获取组队信息*/
    Map queryRecruitByUserId(Integer pageNum, Integer pageSize, Long userId);
    /*分页获取所有的组队信息*/
    Map queryRecruit(Integer pageNum, Integer pageSize);

}
