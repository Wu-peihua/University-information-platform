package com.example.uipservice.service;

import com.example.uipservice.entity.ForumPosts;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ForumPostsService {

    // 新建一条帖子
    boolean insertPost(ForumPosts post);

    // 根据id删除帖子
    boolean deletePost(Long infoId);

    // 根据id获取帖子
    Map selectPostsById(Long userId);

    // 根据id更新帖子
    boolean updatePost(ForumPosts post);

    // 分页获取帖子
    Map queryPosts();
}
