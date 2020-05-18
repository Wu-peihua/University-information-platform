package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.ResourceMapper;
import com.example.uipservice.dao.UserInfoMapper;
import com.example.uipservice.entity.Resource;
import com.example.uipservice.entity.UserInfo;
import com.example.uipservice.service.ResourceService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    ResourceMapper resourceMapper;
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public boolean insertResource(Resource resource) {
        if (resource.getUserId() != null) {
            try {
                int effectNum = resourceMapper.insert(resource);
                if (effectNum > 0) {
                    return true;
                } else {
                    throw new RuntimeException("服务器错误，发布组队信息失败！");
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("发布组队信息用户的id为空！");
        }
    }

    @Override
    public boolean deleteResourceByInfoId(Long infoId) {
        if (infoId != null) {
            try {
                int effectNum = resourceMapper.deleteByPrimaryKey(infoId);
                if (effectNum > 0) {
                    return true;
                } else {
                    throw new RuntimeException("服务器错误，删除组队信息失败！");
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("删除组队信息用户的id为空！");
        }
    }

    @Override
    public Map queryResourceByUserId(Integer pageNum, Integer pageSize, Long userId) {
        Map resourceMap = new HashMap();
        PageHelper.startPage(pageNum, pageSize);
        Page<Resource> data = resourceMapper.queryResourceByUserId(userId);
        UserInfo userInfo;
        for (Resource resource : data) {
            userInfo = userInfoMapper.selectByPrimaryKey(resource.getUserId());
            resource.setUserName(userInfo.getUserName());
            resource.setPortrait(userInfo.getPortrait());
        }
        resourceMap.put("resInfoList", data);
        resourceMap.put("total", data.getTotal());
        resourceMap.put("pageSize", data.getPageSize());
        resourceMap.put("pageNum", pageNum);

        return resourceMap;
    }

    @Override
    public Map queryResourceByTypeAndKeyword(Integer pageNum, Integer pageSize, Integer subjectId, Integer typeId, String keyword) {
        Map resourceMap = new HashMap();
        PageHelper.startPage(pageNum, pageSize);
        Page<Resource> data;
        if (keyword == null || keyword == "") {
            if (subjectId == 0 && typeId == 0)
                data = resourceMapper.queryResource();
            else if (typeId == 0)
                data = resourceMapper.queryResourceBySubjectId(subjectId);
            else if (subjectId == 0)
                data = resourceMapper.queryResourceByTypeId(typeId);
            else
                data = resourceMapper.queryResourceByType(subjectId, typeId);
        } else {
            keyword = "%" + keyword + "%";
            if (subjectId == 0 && typeId == 0)
                data = resourceMapper.queryResourceByKeyword(keyword);
            else if (typeId == 0)
                data = resourceMapper.queryResourceBySubjectIdAndKeyword(subjectId, keyword);
            else if (subjectId == 0)
                data = resourceMapper.queryResourceByTypeIdAndKeyword(typeId, keyword);
            else
                data = resourceMapper.queryResourceByTypeAndKeyword(subjectId, typeId, keyword);
        }
        UserInfo userInfo;
        for (Resource resource : data) {
            userInfo = userInfoMapper.selectByPrimaryKey(resource.getUserId());
            resource.setUserName(userInfo.getUserName());
            resource.setPortrait(userInfo.getPortrait());
        }
        resourceMap.put("resInfoList", data);
        resourceMap.put("total", data.getTotal());
        resourceMap.put("pageSize", data.getPageSize());
        resourceMap.put("pageNum", pageNum);

        return resourceMap;
    }
}
