package com.example.uipservice.dao;

import com.example.uipservice.entity.ForumPosts;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ForumPostsMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(ForumPosts record);

    int insertSelective(ForumPosts record);

    ForumPosts selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(ForumPosts record);

    int updateByPrimaryKey(ForumPosts record);

    List<ForumPosts> selectPostsById(Long userId);

    List<ForumPosts> queryPosts();
}