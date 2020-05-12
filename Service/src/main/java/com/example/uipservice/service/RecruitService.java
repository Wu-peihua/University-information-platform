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

    //根据id修改组队举报数量
    boolean modifyRecruitReportNumber(Long infoId);

    //分页获取所有组队信息
    /*根据发布人的用户id分页获取组队信息*/
    Map queryRecruitByUserId(Integer pageNum, Integer pageSize, Long userId);
    //根据信息主键id分页获取
    Map queryRecruitByInfoId(Integer pageNum, Integer pageSize, Long infoId);
    /*根据学校ID和学院ID分页获取所有组队信息*/
    Map queryRecruitByUniAndIns(Integer pageNum, Integer pageSize, Integer universityId,Integer instituteId);
    /*分页获取所有的组队信息*/
    Map queryRecruit(Integer pageNum, Integer pageSize);
    /*举报反馈，根据学校ID和学院ID分页获取举报数大于50的组队信息*/
    Map queryRecruitByUniAndInsReport(Integer pageNum, Integer pageSize, Integer universityId,Integer instituteId);
    /*举报反馈分页获取举报数大于50的的组队信息*/
    Map queryRecruitReport(Integer pageNum, Integer pageSize);

}
