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

    /*根据发布人的用户id分页获取组队信息*/
    Page<Recruit> queryRecruitByUserId(Long userId);
    /*分页获取所有的组队信息*/
    Page<Recruit> queryRecruit();
}