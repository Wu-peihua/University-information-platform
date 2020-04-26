package com.example.uipservice.dao;

import com.example.uipservice.entity.Recruit;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecruitMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(Recruit record);

    int insertSelective(Recruit record);

    Recruit selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(Recruit record);

    int updateByPrimaryKey(Recruit record);

    /*分页获取*/
    Page<Recruit> queryRecruit();
    //根据学校id和专业id获取
    Page<Recruit> queryRecruitByUniAndIns(Integer universityId,Integer instituteId);
    //根据发布者id获取
    Page<Recruit> queryRecruitByUserId(Long userId);
    //根据主键id获取
    Page<Recruit> queryRecruitByInfoId(Long infoId);



}