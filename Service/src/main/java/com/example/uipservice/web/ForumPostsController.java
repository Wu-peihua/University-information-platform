package com.example.uipservice.web;

import com.example.uipservice.entity.ForumPosts;
import com.example.uipservice.service.ForumPostsService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Map<String, Object> insertPost(ForumPosts post) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", forumPostsService.insertPost(post));
        return modelMap;
    }

    /**
     * 删除帖子
     * @return modelMap
     */
    @RequestMapping(value = "/deletepost", method = RequestMethod.POST)
    private Map<String, Object> deletePost(Long infoId) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", forumPostsService.deletePost(infoId));
        return modelMap;
    }

    /**
     * 更新帖子
     * @return modelMap
     */
    @RequestMapping(value = "/updatepost", method = RequestMethod.POST)
    private Map<String, Object> updatePost(ForumPosts post) {
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("success", forumPostsService.updatePost(post));
        return modelMap;
    }

    /**
     * 获取帖子
     * @return modelMap
     */
    @RequestMapping(value = "/queryposts", method = RequestMethod.GET)
    private Map queryPosts() {
        return forumPostsService.queryPosts();
    }
}
