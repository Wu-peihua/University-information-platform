package com.example.uipfrontend.Entity;

import java.util.Date;

public class CourseComment {
    private Integer CommentId;
    private Integer UserId;
    private String UserName;
    private String UserImg;
    private Date CommentDate;
    private String Content;
    private double Score;
    private static int BadReportCount=0;//举报次数
    private static int LikeCount=0;//点赞次数

    public CourseComment(String _name, Date _commentdate,String _content,double _score){
        this.UserName = _name;
        this.CommentDate = _commentdate;
        this.Content = _content;
        this.Score = _score;
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

    public  void setUserImg(String urlImg) {
        this.UserImg = urlImg;
    }

    public void setCommentDate(Date commentDate) {
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

    public  String getUserImg() {
        return this.UserImg;
    }

    public Date getCommentDate() {
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

    @Override
    public String toString(){
        return "CourseComment[ commentId:" + CommentId + ",UserId:" + UserId + ",UserImg:" + UserImg + ",Content:" + Content + ",CommentDate:" + CommentDate
                + ",userName:" + UserName +
                ",score:" + Score + "]";
    }
}