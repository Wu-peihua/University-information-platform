package com.example.uipservice.dao;

import com.example.uipservice.entity.ReleaseRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Service
public interface ReleaseRecordMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(ReleaseRecord record);

    int insertSelective(ReleaseRecord record);

    ReleaseRecord selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(ReleaseRecord record);

    int updateByPrimaryKey(ReleaseRecord record);
}