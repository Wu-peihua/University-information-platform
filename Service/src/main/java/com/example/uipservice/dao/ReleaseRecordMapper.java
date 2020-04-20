package com.example.uipservice.dao;

import com.example.uipservice.entity.ReleaseRecord;

public interface ReleaseRecordMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(ReleaseRecord record);

    int insertSelective(ReleaseRecord record);

    ReleaseRecord selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(ReleaseRecord record);

    int updateByPrimaryKey(ReleaseRecord record);
}