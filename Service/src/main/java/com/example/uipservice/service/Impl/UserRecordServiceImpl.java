package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.UserRecordMapper;
import com.example.uipservice.entity.UserRecord;
import com.example.uipservice.service.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserRecordServiceImpl implements UserRecordService {

    @Autowired
    UserRecordMapper userRecordMapper;

    @Override
    public Long insertRecord(UserRecord record) {
        try {
            int effectNum = userRecordMapper.insert(record);
            if (effectNum > 0) return record.getInfoId();
            else throw new RuntimeException("服务器错误！插入记录失败！");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean deleteRecord(Long infoId) {
        try {
            int effectNum = userRecordMapper.delete(infoId);
            if (effectNum > 0) return true;
            else throw new RuntimeException("服务器错误！删除记录失败！");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map queryRecord(Long userId) {
        Map resMap = new HashMap();
        try {
            List<UserRecord> likeRecord = userRecordMapper.queryByUserId(userId, 1);
            List<UserRecord> reportRecord = userRecordMapper.queryByUserId(userId, 2);
            resMap.put("likeRecord", likeRecord);
            resMap.put("reportRecord", reportRecord);
            return resMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
