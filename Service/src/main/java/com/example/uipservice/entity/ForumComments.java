package com.example.uipservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class ForumComments {
    private Long infoId;

    private Long toId;

    private Long fromId;

    private String fromName;

    private String portrait;

    private String content;

    private Integer reportNumber;

    private Integer likeNumber;

    private Integer replyNumber;

    private Date infoDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date created;

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(Integer reportNumber) {
        this.reportNumber = reportNumber;
    }

    public Integer getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        this.likeNumber = likeNumber;
    }

    public Integer getReplyNumber() { return replyNumber; }

    public void setReplyNumber(Integer replyNumber) {
        this.replyNumber = replyNumber;
    }

    public Date getInfoDate() {
        return infoDate;
    }

    public void setInfoDate(Date infoDate) {
        this.infoDate = infoDate;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
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
}