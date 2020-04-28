package com.example.uipfrontend.Entity;

import java.io.Serializable;
import java.util.List;

// 论坛帖子
public class ForumPosts implements Serializable {
    private Long postId;            // 帖子ID
    private String title;           // 帖子标题
    private String content;         // 帖子内容
    private String created;         // 发布时间
    private List<String> pictures;  // 图片
    private int replyNumber;        // 评论数
    private int likeNumber;         // 点赞数
    private int reportNumber;       // 举报数
    private Long userId;            // 发帖人ID
    private String userName;        // 发帖人昵称
    private String portrait;        // 发帖人头像
    
    public ForumPosts() {}

    public ForumPosts(Long postId, String title, String userName, String postTime,
                      int likeNum) {
        this.postId = postId;
        this.title = title;
        this.userName = userName;
        this.created = postTime;
        this.likeNumber = likeNum;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }
    
    public int getReplyNumber() { return replyNumber; }
    
    public void setReplyNumber(int replyNumber) { this.replyNumber = replyNumber; }
    
    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        // 从用户表获取用户名
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPortrait() {
        // 从用户表获取
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
