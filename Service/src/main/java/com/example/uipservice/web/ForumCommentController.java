package com.example.uipservice.web;

import com.example.uipservice.entity.ForumComments;
import com.example.uipservice.service.ForumCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/postcomment")
public class ForumCommentController {

    @Autowired
    ForumCommentService forumCommentService;

    /**
     * 插入评论
     * @return modelMap
     */
    @RequestMapping(value = "/insertcomment", method = RequestMethod.POST)
    private Map<String, Object> insertComment(@RequestBody ForumComments comment) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("resId", forumCommentService.insertComment(comment));
        return modelMap;
    }

    /**
     * 删除评论
     * @return modelMap
     */
    @RequestMapping(value = "/deletecomment", method = RequestMethod.POST)
    private Map<String, Object> deleteComment(Long infoId) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("result", forumCommentService.deleteComment(infoId));
        return modelMap;
    }

    /**
     * 查找评论
     * @return modelMap
     */
    @RequestMapping(value = "/querycomments", method = RequestMethod.GET)
    private Map queryComments(Integer pageNum, Integer pageSize, Long infoId) {
        return forumCommentService.queryComments(pageNum, pageSize, infoId);
    }
}
