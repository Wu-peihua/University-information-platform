package com.example.uipfrontend.Entity;

import java.io.Serializable;
import java.util.List;

// 论坛帖子
public class ForumPosts implements Serializable {
    private Integer postId;         // 帖子ID
    private String title;           // 帖子标题
    private String content;         // 帖子内容
    private String postTime;        // 发布时间
    private List<String> pictures;  // 图片
    private int likeNum;            // 点赞数
    private int reportNum;          // 举报数
    private Integer userId;         // 发帖人ID
    private String poster;          // 发帖人用户名
    private String portrait;        // 发帖人头像

    public ForumPosts(Integer postId, String title, String poster, String postTime,
                      int likeNum) {
        this.postId = postId;
        this.title = title;
        this.poster = poster;
        this.postTime = postTime;
        this.likeNum = likeNum;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
    
    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getReportNum() {
        return reportNum;
    }

    public void setReportNum(int reportNum) {
        this.reportNum = reportNum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPoster() {
        // 从用户表获取用户名
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPortrait() {
        // 从用户表获取
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
