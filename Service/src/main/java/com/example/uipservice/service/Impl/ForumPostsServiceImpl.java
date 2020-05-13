package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.ForumPostsMapper;
import com.example.uipservice.entity.ForumPosts;
import com.example.uipservice.service.ForumPostsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
                int effectNum = forumPostsMapper.insertSelective(post);
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
    public Map selectPostsById(Integer pageNum, Integer pageSize, Long userId) {
        Map resMap = new HashMap();
        PageHelper.startPage(pageNum, pageSize);
        try {
            Page<ForumPosts> res = forumPostsMapper.selectPostsById(userId);
            resMap.put("postsList", res);
            resMap.put("total", res.getTotal());
            resMap.put("pageSize", res.getPageSize());
            resMap.put("pageNum", pageNum);
            return resMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean updatePost(ForumPosts post) {
        if (post.getTitle() != null && post.getContent() != null) {
            try {
                int effectNum = forumPostsMapper.updateByPrimaryKeySelective(post);
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
    public Map queryPosts(Integer pageNum, Integer pageSize) {
        Map resMap = new HashMap();
        PageHelper.startPage(pageNum, pageSize);
        try {
            Page<ForumPosts> res = forumPostsMapper.queryPosts();
            resMap.put("postsList", res);
            resMap.put("total", res.getTotal());
            resMap.put("pageSize", res.getPageSize());
            resMap.put("pageNum", pageNum);
            return resMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map queryPostsByKeyword(Integer pageNum, Integer pageSize, String keyword) {
        Map resMap = new HashMap();
        keyword = "%" + keyword + "%";
        // todo 全文索引
        // match(title) against(#{keyword,jdbcType=VARCHAR} in boolean mode)
        // keyword = "*" + keyword + "*";
        PageHelper.startPage(pageNum, pageSize);
        try {
            Page<ForumPosts> res = forumPostsMapper.queryPostsByKeyword(keyword);
            resMap.put("postsList", res);
            resMap.put("total", res.getTotal());
            resMap.put("pageSize", res.getPageSize());
            resMap.put("pageNum", pageNum);
            return resMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
