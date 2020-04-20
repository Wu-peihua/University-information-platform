package com.example.uipfrontend.Entity;

import java.io.Serializable;
import java.util.Date;

public class CourseComment implements Serializable {
    private Integer CourseId;//评论的课程id
    private String CourseName;//课程名称
    private Integer CommentId;//评论的id
    private Integer UserId;//用户id
    private String UserName;//用户名
    private String UserImgUrl;//用户头像
    private String CommentDate;//评论日期
    private String Content;//评论内容
    private double Score;//评分
    private int BadReportCount;//举报次数
    private int LikeCount;//点赞次数


    public CourseComment(String courseName,Integer commentId, String _name, String _commentdate,String _content,double _score,int likeCount){
        this.CourseName = courseName;
        this.CommentId = commentId;
        this.UserName = _name;
        this.CommentDate = _commentdate;
        this.Content = _content;
        this.Score = _score;
        this.LikeCount=likeCount;
    }

    public CourseComment(Integer commentId, String _name, String _commentdate,String _content,double _score,int likeCount){
        this.CommentId = commentId;
        this.UserName = _name;
        this.CommentDate = _commentdate;
        this.Content = _content;
        this.Score = _score;
        this.LikeCount=likeCount;
    }

    public void setCourseId(Integer courseId){
        this.CourseId = courseId;
    }

    public  void setCourseName(String courseName){
        this.CourseName = courseName;
    }

    public void setCommentId(Integer commentId){ this.CommentId = commentId;}

    public  void setUserName(String _name) {
        this.UserName = _name;
    }

    public void setUserId(Integer userId) {
        this.UserId = userId;
    }

    public  void setScore(double _score) {
        this.Score = _score;
    }

    public  void setUserImgUrl(String urlImg) {
        this.UserImgUrl = urlImg;
    }

    public void setCommentDate(String commentDate) {
        this.CommentDate = commentDate;
    }

    public  void setContent(String content) {
        this.Content = content;
    }

    public void setBadReportCount(int bad) {
        this.BadReportCount = bad;
    }

    public void setLikeCount(int like) {
        this.LikeCount = like;
    }

    public String getUserName() {
        return this.UserName;
    }

    public  String getUserImgUrl() {
        return this.UserImgUrl;
    }

    public String getCommentDate() {
        return this.CommentDate;
    }

    public  String getContent() {
        return this.Content;
    }

    public  int getBadReportCount() {
        return  this.BadReportCount;
    }

    public int getLikeCount() {
        return this.LikeCount;
    }

    public double getScore(){
        return this.Score;
    }

    public Integer getCommentId() {
        return this.CommentId;
    }

    public Integer getCourseId() {return this.CourseId;}

    public String getCourseName() {return this.CourseName;}
    @Override
    public String toString(){
        return "CourseComment[ commentId:" + CommentId + ",UserId:" + UserId + ",UserImg:" + UserImgUrl + ",Content:" + Content + ",CommentDate:" + CommentDate
                + ",userName:" + UserName +
                ",score:" + Score + "]";
    }
}