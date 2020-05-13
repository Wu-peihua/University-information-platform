package com.example.uipservice.dao;

import com.example.uipservice.entity.ForumComments;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ForumCommentsMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(ForumComments record);

    int insertSelective(ForumComments record);

    ForumComments selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(ForumComments record);

    int updateByPrimaryKey(ForumComments record);

    Page<ForumComments> queryComments(Long toId);

    Page<ForumComments> queryCommentsByCreated(Long toId);

    Page<ForumComments> queryCommentsByCreatedDesc(Long toId);
}