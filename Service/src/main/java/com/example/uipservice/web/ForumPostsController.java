package com.example.uipservice.web;

import com.example.uipservice.entity.ForumPosts;
import com.example.uipservice.service.ForumPostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/forumposts")
public class ForumPostsController {

    @Autowired
    ForumPostsService forumPostsService;

    /**
     * 插入帖子
     * @return modelMap
     */
    @RequestMapping(value = "/insertpost", method = RequestMethod.POST)
    private Map<String, Object> insertPost(@RequestBody ForumPosts post) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("result", forumPostsService.insertPost(post));
        return modelMap;
    }

    /**
     * 删除帖子
     * @return modelMap
     */
    @RequestMapping(value = "/deletepost", method = RequestMethod.POST)
    private Map<String, Object> deletePost(Long infoId) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("result", forumPostsService.deletePost(infoId));
        return modelMap;
    }

    /**
     * 根据ID获取帖子
     * @return modelMap
     */
    @RequestMapping(value = "/selectposts", method = RequestMethod.GET)
    private Map selectPosts(Integer pageNum, Integer pageSize, Long userId) {
        return forumPostsService.selectPostsById(pageNum, pageSize, userId);
    }

    /**
     * 更新帖子
     * @return modelMap
     */
    @RequestMapping(value = "/updatepost", method = RequestMethod.POST)
    private Map<String, Object> updatePost(@RequestBody ForumPosts post) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("result", forumPostsService.updatePost(post));
        return modelMap;
    }

    /**
     * 获取帖子
     * @return modelMap
     */
    @RequestMapping(value = "/queryposts", method = RequestMethod.GET)
    private Map queryPosts(Integer pageNum, Integer pageSize) {
        return forumPostsService.queryPosts(pageNum, pageSize);
    }
}
