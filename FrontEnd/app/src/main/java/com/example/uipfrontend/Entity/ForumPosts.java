package com.example.uipfrontend.Entity;

import java.io.Serializable;

public class ForumPosts implements Serializable {
    private Integer postId;
    private String title;
    private String content;
    private String poster;
    private String postTime;
    private int likeNum;
    private int reportNum;

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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
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
}
