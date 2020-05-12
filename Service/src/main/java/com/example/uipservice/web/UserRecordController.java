package com.example.uipservice.web;

import com.example.uipservice.entity.UserRecord;
import com.example.uipservice.service.UserRecordService;
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
}
