package com.example.uipservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CourseEvaluation {
    private Long infoId;

    private Long commentatorId;

    private String fromName;

    private String portrait;

    private Long courseId;

    private String content;

    private Integer score;

    private Integer reportNumber;

    private  Integer likeNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",  timezone="GMT+8")
    private Date infoDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",  timezone="GMT+8")
    private Date created;

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public Long getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(Long commentatorId) {
        this.commentatorId = commentatorId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getReportNumber(){return this.reportNumber;}

    public  void setReportNumber(Integer report_number){this.reportNumber = report_number;}

    public  Integer getLikeNumber(){return this.likeNumber;}

    public  void setLikeNumber(Integer like_number){this.likeNumber = like_number;}

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