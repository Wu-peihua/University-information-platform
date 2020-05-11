package com.example.uipservice.web;

import com.example.uipservice.entity.Resource;
import com.example.uipservice.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    ResourceService resourceService;

    /**
     * 描述：插入资源信息
     * @param resource
     * @return
     */
    @RequestMapping(value = "/insertresource", method = RequestMethod.POST)
    private Map<String, Object> insertResource(@RequestBody Resource resource) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", resourceService.insertResource(resource));
        return modelMap;
    }

    /**
     * 描述：根据主键id删除资源发布记录
     * @param infoId
     * @return
     */
    @RequestMapping(value = "/deleteresourcebyinfoid", method = RequestMethod.POST)
    private Map<String, Object> deleteResourceByInfoId(Long infoId) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", resourceService.deleteResourceByInfoId(infoId));
        return modelMap;
    }

    /**
     * 描述：根据用户id获取资源信息
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    @RequestMapping(value = "/queryresourcebyuserid", method = RequestMethod.GET)
    private Map queryResourceByUserId(Integer pageNum, Integer pageSize, Long userId) {
        return resourceService.queryResourceByUserId(pageNum, pageSize, userId);
    }

    /**
     * 描述：根据资源类型获取资源信息
     * @param pageNum
     * @param pageSize
     * @param subjectId
     * @param typeId
     * @return
     */
    @RequestMapping(value = "/queryresourcebytype", method = RequestMethod.GET)
    private Map queryResourceByType(Integer pageNum, Integer pageSize, Integer subjectId, Integer typeId) {
        if (subjectId == 0 && typeId == 0)
            return resourceService.queryResource(pageNum, pageSize);
        else
            return resourceService.queryResourceByType(pageNum, pageSize, subjectId, typeId);
    }
}
