package com.example.uipservice.dao;

import com.example.uipservice.entity.UserRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRecordMapper {

    int insert(UserRecord record);

    int delete(Long infoId);

    List<UserRecord> queryByUserId(@Param("userId")Long userId, @Param("tag")int tag);

    UserRecord queryByUserIdAndObjectIdAndTagAndType(@Param("userId")Long userId,@Param("objectId")Long objectId, @Param("tag")int tag,@Param("type")Integer type);

}
