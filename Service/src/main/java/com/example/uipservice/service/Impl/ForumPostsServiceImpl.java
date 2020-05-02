package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.ForumPostsMapper;
import com.example.uipservice.entity.ForumPosts;
import com.example.uipservice.service.ForumPostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ForumPostsServiceImpl implements ForumPostsService {

    @Autowired
    ForumPostsMapper forumPostsMapper;

    @Override
    public boolean insertPost(ForumPosts post) {
        if (post.getTitle() != null && post.getContent() != null) {
            try {
                int effectNum = forumPostsMapper.insert(post);
                if (effectNum > 0) return true;
                else throw new RuntimeException("服务器错误！插入帖子失败！");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("插入的帖子的标题或内容为空");
        }
    }

    @Override
    public boolean deletePost(Long infoId) {
        if (infoId != null) {
            try {
                int effectNum = forumPostsMapper.deleteByPrimaryKey(infoId);
                if (effectNum > 0) return true;
                else throw new RuntimeException("服务器错误！删除帖子失败！");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("删除的帖子的id为空！");
        }
    }

    @Override
    public Map selectPostsById(Long userId) {
        Map resMap = new HashMap();
        try {
            List<ForumPosts> postsList = forumPostsMapper.selectPostsById(userId);
            resMap.put("postsList", postsList);
            return resMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean updatePost(ForumPosts post) {
        if (post.getTitle() != null && post.getContent() != null) {
            try {
                int effectNum = forumPostsMapper.updateByPrimaryKey(post);
                if (effectNum > 0) return true;
                else throw new RuntimeException("服务器错误！更新帖子失败！");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("更新的帖子的标题或内容为空");
        }
    }

    @Override
    public Map queryPosts() {
        Map resMap = new HashMap();
        try {
            List<ForumPosts> postsList = forumPostsMapper.queryPosts();
            resMap.put("postsList", postsList);
            return resMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
