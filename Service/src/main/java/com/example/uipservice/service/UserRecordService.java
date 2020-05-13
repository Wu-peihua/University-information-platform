package com.example.uipservice.service;

import com.example.uipservice.entity.UserRecord;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserRecordService {

    Long insertRecord(UserRecord record);

    boolean deleteRecord(Long infoId);

    Map queryRecord(Long userId);

    UserRecord queryByUserIdAndObjectIdAndTagAndType(Long userId,Long objectId,Integer tag,Integer type);

    //插入举报记录
    boolean insertReportRecord(UserRecord userRecord);

}
