package com.example.uipservice.dao;

import com.example.uipservice.entity.CommentReply;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentReplyMapper {
    int insert(CommentReply reply);

    int insertSelective(CommentReply reply);

    int deleteByPrimaryKey(Long infoId);

    Page<CommentReply> queryReply(Long commentId);

    Page<CommentReply> queryReplyByCreated(Long commentId);

    Page<CommentReply> queryReplyByCreatedDesc(Long commentId);
}
