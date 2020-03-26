package com.example.uipfrontend.Entity;

public class ForumPosts {
    private String postId;
    private String title;
    private String content;
    private String poster;
    private String postTime;
    private int likeNum;
    private int reportNum;

    public ForumPosts(String title, String poster, String postTime,
                      int likeNum, int reportNum) {
        this.title = title;
        this.poster = poster;
        this.postTime = postTime;
        this.likeNum = likeNum;
        this.reportNum = reportNum;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
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
