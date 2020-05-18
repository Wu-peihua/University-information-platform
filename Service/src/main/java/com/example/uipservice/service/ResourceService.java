package com.example.uipservice.service;

import com.example.uipservice.entity.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ResourceService {
    boolean insertResource(Resource resource);

    boolean deleteResourceByInfoId(Long infoId);

    Map queryResourceByUserId(Integer pageNum, Integer pageSize, Long userId);

    Map queryResourceByTypeAndKeyword(Integer pageNum, Integer pageSize, Integer subjectId, Integer typeId,String keyword);
}
