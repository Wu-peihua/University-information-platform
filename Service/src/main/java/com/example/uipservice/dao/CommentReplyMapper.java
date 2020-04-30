package com.example.uipservice.dao;

import com.example.uipservice.entity.CommentReply;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentReplyMapper {
    int insert(CommentReply reply);

    int deleteByPrimaryKey(Long infoId);

    List<CommentReply> queryReply(Long commentId);
}
