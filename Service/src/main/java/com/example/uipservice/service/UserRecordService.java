package com.example.uipservice.service;

import com.example.uipservice.entity.UserRecord;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserRecordService {

    Long insertRecord(UserRecord record);

    boolean deleteRecord(Long infoId);

    Map queryRecord(Long userId);

}
