package com.example.uipservice.service;

import com.example.uipservice.entity.CommentReply;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface CommentReplyService {
    // 新建一条评论
    boolean insertReply(CommentReply reply);

    // 删除一条评论
    boolean deleteReply(Long infoId);

    // 获取评论
    Map queryReply(Long infoId);
}
