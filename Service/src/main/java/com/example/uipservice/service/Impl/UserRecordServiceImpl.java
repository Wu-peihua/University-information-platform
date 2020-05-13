package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.UserRecordMapper;
import com.example.uipservice.entity.UserInfo;
import com.example.uipservice.entity.UserRecord;
import com.example.uipservice.service.UserRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
            int effectNum = userRecordMapper.insertSelective(record);
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

    @Override
    public UserRecord queryByUserIdAndObjectIdAndTagAndType(Long userId, Long objectId, Integer tag, Integer type) {
        if(userId != null && objectId != null && tag != null && type != null){
            try {
                UserRecord userRecord = userRecordMapper.queryByUserIdAndObjectIdAndTagAndType(userId,objectId,tag,type);
                return userRecord;
            }catch (Exception e){
                throw new RuntimeException("服务器操作错误: " + e.getMessage());
            }
        }else{
            throw new RuntimeException("查找缺失参数！");
        }
    }

    @Override
    public boolean insertReportRecord(UserRecord userRecord) {
        if(userRecord.getUserId() != null && userRecord.getToId() != null ){
            try {
                UserRecord record = userRecordMapper.queryByUserIdAndObjectIdAndTagAndType(userRecord.getUserId(),userRecord.getToId(),userRecord.getTag(),userRecord.getType());
                if (record == null){  //找不到插入记录，则插入举报记录
                    userRecord.setCreated(new Date());
                    int effectNum = userRecordMapper.insert(userRecord);
                    if (effectNum > 0){
                        System.out.println("举报成功");
                        return true;
                    }else
                        throw new RuntimeException("服务器错误！举报记录插入失败！");
                }else{   //找到插入记录，则返回已举报信息
                    System.out.println("举报失败");
                    return false;
                }
            }catch (Exception e){
                throw new RuntimeException("服务器操作错误: " + e.getMessage());
            }
        }else{
            throw new RuntimeException("插入缺失参数！");
        }
    }


}
