package com.example.uipservice.dao;

import com.example.uipservice.entity.ForumPosts;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface ForumPostsMapper {
    int deleteByPrimaryKey(Long infoId);

    int insert(ForumPosts record);

    int insertSelective(ForumPosts record);

    ForumPosts selectByPrimaryKey(Long infoId);

    int updateByPrimaryKeySelective(ForumPosts record);

    int updateByPrimaryKey(ForumPosts record);

    Page<ForumPosts> selectPostsById(Long userId);

    Page<ForumPosts> queryPosts();

    Page<ForumPosts> queryPostsByKeyword(@Param("keyword") String keyword);
}