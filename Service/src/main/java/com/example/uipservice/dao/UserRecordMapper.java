package com.example.uipservice.dao;

import com.example.uipservice.entity.UserRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRecordMapper {

    int insert(UserRecord record);

    int delete(Long infoId);

    List<UserRecord> queryByUserId(Long userId, int tag);
}
