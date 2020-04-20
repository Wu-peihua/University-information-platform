package com.example.uipservice.dao;

import com.example.uipservice.entity.ForumPosts;

public interface ForumPostsMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(ForumPosts record);

    int insertSelective(ForumPosts record);

    ForumPosts selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(ForumPosts record);

    int updateByPrimaryKey(ForumPosts record);
}