package com.example.uipfrontend.Entity;

import java.io.Serializable;

// 评论：包括帖子的评论和评论的评论
public class PostComment implements Serializable {
    private Long    infoId;       // 评论ID
    private String  content;      // 评论内容
    private String  created;      // 发表时间
    private Integer replyNumber;  // 评论的回复数
    private Integer likeNumber;   // 点赞数
    private Integer reportNumber; // 举报数
    private Long    fromId;       // 评论者ID
    private String  fromName;     // 评论者用户名
    private String  portrait;     // 评论者头像
    private Long    toId;         // 所评论的帖子或评论的ID
    private String  toName;       // 被评论者用户名
    private String  reference;    // 被评论的内容

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
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
    
    public Integer getReplyNumber() { return replyNumber; }
    
    public void setReplyNumber(Integer replyNumber) { this.replyNumber = replyNumber; }

    public Integer getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        this.likeNumber = likeNumber;
    }

    public Integer getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(Integer reportNumber) {
        this.reportNumber = reportNumber;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
