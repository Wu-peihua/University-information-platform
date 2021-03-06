package com.example.uipfrontend.Entity;

import android.app.Application;

import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.Map;

public class UserInfo extends Application {


    public UserInfo() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
    }

    public String getStuCard() {
        return stuCard;
    }

    public void setStuCard(String stuCard) {
        this.stuCard = stuCard;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Integer getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Integer universityId) {
        this.universityId = universityId;
    }

    public Integer getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Integer instituteId) {
        this.instituteId = instituteId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    
    public Map<String, Long> getLikeRecord() { return likeRecord; }
    
    public Map<String, Long> getReportRecord() { return reportRecord; }
    
    public void setLikeRecord(Map<String, Long> record) { this.likeRecord = record; }
    
    public void setReportRecord(Map<String, Long> record) { this.reportRecord = record; }

    @Expose
    private Long userId;

    @Expose
    private String userName;

    @Expose
    private String pw;

    @Expose
    private String stuNumber;

    @Expose
    private String stuCard;

    @Expose
    private String portrait;

    @Expose
    private Integer universityId;

    @Expose
    private Integer instituteId;

    @Expose
    private Integer userType;

    @Expose
    private Date created;

    @Expose(serialize = false)
    private Map<String, Long> likeRecord;

    @Expose(serialize = false)
    private Map<String, Long> reportRecord;



}