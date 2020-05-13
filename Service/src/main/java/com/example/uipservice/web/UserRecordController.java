package com.example.uipservice.web;

import com.example.uipservice.entity.UserRecord;
import com.example.uipservice.service.UserRecordService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/userrecord")
public class UserRecordController {

    @Autowired
    UserRecordService userRecordService;

    /**
     * 插入记录
     * @return modelMap
     */
    @RequestMapping(value = "/insertrecord", method = RequestMethod.POST)
    private Map<String, Object> insertRecord(@RequestBody UserRecord record) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("result", userRecordService.insertRecord(record));
        return modelMap;
    }

    /**
     * 删除记录
     * @return modelMap
     */
    @RequestMapping(value = "/deleterecord", method = RequestMethod.POST)
    private Map<String, Object> deleteRecord(Long infoId) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("result", userRecordService.deleteRecord(infoId));
        return modelMap;
    }

    /**
     *  查询记录
     *  @return modelMap
     */
    @RequestMapping(value = "/queryrecord", method = RequestMethod.GET)
    private Map queryRecord(Long userId) {
        return userRecordService.queryRecord(userId);
    }

    /**
     *  查询记录
     *  @return map
     */
    @RequestMapping(value = "/querybyuseridandobjectidandtagandtype", method = RequestMethod.GET)
    private UserRecord queryByUserIdAndObjectIdAndTagAndType(Long userId, Long objectId, Integer tag, Integer type) {
        return userRecordService.queryByUserIdAndObjectIdAndTagAndType(userId,objectId,tag,type);
    }

    @RequestMapping(value = "insertreportrecord",method = RequestMethod.POST)
    private Map insertReportRecord(@RequestBody UserRecord userRecord){
        Map<String ,Object> modelMap = new HashMap<>();
        modelMap.put("result",userRecordService.insertReportRecord(userRecord));
        return  modelMap;
    }
}
