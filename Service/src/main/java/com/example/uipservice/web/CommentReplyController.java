package com.example.uipservice.web;

import com.example.uipservice.entity.CommentReply;
import com.example.uipservice.service.CommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Map<String, Object> insertReply(CommentReply reply) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", commentReplyService.insertReply(reply));
        return modelMap;
    }

    /**
     * 删除回复
     * @return modelMap
     */
    @RequestMapping(value = "/deletereply", method = RequestMethod.POST)
    private Map<String, Object> deleteReply(Long infoId) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", commentReplyService.deleteReply(infoId));
        return modelMap;
    }

    /**
     * 查找回复
     * @return modelMap
     */
    @RequestMapping(value = "/queryreply", method = RequestMethod.GET)
    private Map queryReply(Long infoId) {
        return commentReplyService.queryReply(infoId);
    }
}
