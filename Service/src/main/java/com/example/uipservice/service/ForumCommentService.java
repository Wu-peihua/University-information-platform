package com.example.uipservice.service;

import com.example.uipservice.entity.ForumComments;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ForumCommentService {

    // 新建一条评论
    Long insertComment(ForumComments comment);

    // 删除一条评论
    boolean deleteComment(Long infoId);

    // 获取评论
    Map queryComments(Integer pageNum, Integer pageSize, Long infoId);

}
