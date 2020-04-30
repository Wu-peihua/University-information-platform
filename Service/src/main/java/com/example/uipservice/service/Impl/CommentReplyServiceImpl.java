package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.CommentReplyMapper;
import com.example.uipservice.entity.CommentReply;
import com.example.uipservice.service.CommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CommentReplyServiceImpl implements CommentReplyService {

    @Autowired
    CommentReplyMapper commentReplyMapper;

    @Override
    public boolean insertReply(CommentReply reply) {
        if (reply.getContent() != null) {
            try {
                int effectNum = commentReplyMapper.insert(reply);
                if (effectNum > 0) return true;
                else throw new RuntimeException("服务器错误！插入回复失败！");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("插入的回复的内容为空");
        }
    }

    @Override
    public boolean deleteReply(Long infoId) {
        if (infoId != null) {
            try {
                int effectNum = commentReplyMapper.deleteByPrimaryKey(infoId);
                if (effectNum > 0) return true;
                else throw new RuntimeException("服务器错误！删除回复失败！");
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new RuntimeException("删除的回复的id为空！");
        }
    }

    @Override
    public Map queryReply(Long infoId) {
        Map resMap = new HashMap();
        try {
            List<CommentReply> replyList = commentReplyMapper.queryReply(infoId);
            resMap.put("replyList", replyList);
            return resMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
