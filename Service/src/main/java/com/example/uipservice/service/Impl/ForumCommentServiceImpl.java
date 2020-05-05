package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.ForumCommentsMapper;
import com.example.uipservice.entity.ForumComments;
import com.example.uipservice.service.ForumCommentService;
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
public class ForumCommentServiceImpl implements ForumCommentService {

    @Autowired
    ForumCommentsMapper forumCommentsMapper;

    @Override
    public Long insertComment(ForumComments comment) {
        if (comment.getContent() != null) {
            try {
                int effectNum = forumCommentsMapper.insert(comment);
                if (effectNum > 0) return comment.getInfoId();
                else throw new RuntimeException("服务器错误！插入评论失败！");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("插入的评论的内容为空");
        }
    }

    @Override
    public boolean deleteComment(Long infoId) {
        if (infoId != null) {
            try {
                int effectNum = forumCommentsMapper.deleteByPrimaryKey(infoId);
                if (effectNum > 0) return true;
                else throw new RuntimeException("服务器错误！删除评论失败！");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("删除的评论的id为空！");
        }
    }

    @Override
    public Map queryComments(Integer pageNum, Integer pageSize, Long infoId) {
        Map resMap = new HashMap();
        PageHelper.startPage(pageNum, pageSize);
        try {
            Page<ForumComments> res = forumCommentsMapper.queryComments(infoId);
            resMap.put("commentList", res);
            resMap.put("total", res.getTotal());
            resMap.put("pageSize", res.getPageSize());
            resMap.put("pageNum", pageNum);
            return resMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
