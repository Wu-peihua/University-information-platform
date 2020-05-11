package com.example.uipservice.dao;

import com.example.uipservice.entity.Resource;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface ResourceMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(Resource record);

    int insertSelective(Resource record);

    Resource selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(Resource record);

    int updateByPrimaryKey(Resource record);
    
    Page<Resource> queryResourceByUserId(Long userId);

    Page<Resource> queryResourceByType(@Param("subjectId") Integer subjectId, @Param("typeId") Integer typeId);

    Page<Resource> queryResource();
}