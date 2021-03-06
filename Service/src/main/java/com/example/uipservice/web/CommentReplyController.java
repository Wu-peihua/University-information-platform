package com.example.uipservice.web;

import com.example.uipservice.entity.CommentReply;
import com.example.uipservice.service.CommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/commentreply")
public class CommentReplyController {

    @Autowired
    CommentReplyService commentReplyService;

    /**
     * 插入回复
     * @return modelMap
     */
    @RequestMapping(value = "/insertreply", method = RequestMethod.POST)
    private Map<String, Object> insertReply(@RequestBody CommentReply reply) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("resId", commentReplyService.insertReply(reply));
        return modelMap;
    }

    /**
     * 删除回复
     * @return modelMap
     */
    @RequestMapping(value = "/deletereply", method = RequestMethod.POST)
    private Map<String, Object> deleteReply(Long infoId) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("result", commentReplyService.deleteReply(infoId));
        return modelMap;
    }

    /**
     * 查找回复
     * @return modelMap
     */
    @RequestMapping(value = "/queryreply", method = RequestMethod.GET)
    private Map queryReply(Integer pageNum, Integer pageSize, Long infoId, int orderMode) {
        return commentReplyService.queryReply(pageNum, pageSize, infoId, orderMode);
    }
}
