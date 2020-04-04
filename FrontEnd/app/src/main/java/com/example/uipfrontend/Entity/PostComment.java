package com.example.uipfrontend.Entity;

// 评论：包括帖子的评论和评论的评论
public class PostComment {
    private Integer id;        // 评论ID
    private String content;    // 评论内容
    private String date;       // 发表时间
    private Integer likeNum;   // 点赞数
    private Integer reportNum; // 举报数
    private Integer from;      // 评论者ID
    private String fromName;   // 评论者用户名
    private String portrait;   // 评论者头像
    private Integer to;        // 所评论的帖子或评论的ID
    private String toName;     // 被评论者用户名

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getReportNum() {
        return reportNum;
    }

    public void setReportNum(Integer reportNum) {
        this.reportNum = reportNum;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public String getFromName() {
        // 从用户表获取
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getPortrait() {
        // 从用户表获取
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getToName() {
        // 从用户表获取
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }
}
