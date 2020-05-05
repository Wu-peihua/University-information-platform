package com.example.uipservice.dao;

import com.example.uipservice.entity.Recruit;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;

@Service
public interface RecruitMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(Recruit record);

    int insertSelective(Recruit record);

    Recruit selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(Recruit record);

    int updateByPrimaryKey(Recruit record);


    Page<Recruit> queryRecruit();

    Page<Recruit> queryRecruitByUniAndIns(Integer universityId,Integer instituteId);

    Page<Recruit> queryRecruitByInfoId(Long infoId);

    Page<Recruit> queryRecruitByUserId(Long userId);
}