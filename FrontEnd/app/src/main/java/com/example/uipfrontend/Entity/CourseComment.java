package com.example.uipfrontend.Entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CourseComment implements Serializable {

    private Long infoId;

    private Long commentatorId;

    private Long courseId;

    private String content;

    private Integer score;


    private Date infoDate;


    private Date created;

    private int BadReportCount;//举报次数
    private int LikeCount;//点赞次数

    private String fromName;//评论用户名字
    private String  portrait;     // 评论者头像

    /*private Integer CourseId;//评论的课程id
    private String CourseName;//课程名称
    private Integer CommentId;//评论的id
    private Integer UserId;//用户id
    private String UserName;//用户名
    private String UserImgUrl;//用户头像
    private String CommentDate;//评论日期
    private String Content;//评论内容
    private double Score;//评分


     */

    //commentatorid + courseid + content+ score+ date


    public CourseComment(Long _userid, Long _courseId,String _content,Integer _score,Date _infodate){


        this.commentatorId = _userid;
        this.courseId = _courseId;
        this.content = _content;
        this.score = _score;
        this.infoDate = _infodate;
        this.LikeCount=0;//新增评论点赞数
        this.BadReportCount = 0;
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


    public  int getBadReportCount() {
        return  this.BadReportCount;
    }

    public int getLikeCount() {
        return this.LikeCount;
    }

    public void setBadReportCount(int _badreport){
        this.BadReportCount = _badreport;
    }

    public void setLikeCount(int likeCount){
        this.LikeCount = likeCount;
    }



    public String toString(){
        return "CourseComment[ commentId:" + infoId + ",UserId:" + commentatorId  + ",Content:" + content + ",CommentDate:" + infoDate
                +",score:" + score + "]";
    }
}