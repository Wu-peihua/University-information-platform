package com.example.uipfrontend.Entity;

import android.app.Application;

import java.util.Date;
import java.util.Map;

public class UserInfo extends Application {
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

    private Long userId;

    private String userName;

    private String pw;

    private String stuNumber;

    private String stuCard;

    private String portrait;

    private Integer universityId;

    private Integer instituteId;

    private Integer userType;

    private Date created;

    private Map<String, Long> likeRecord;
    
    private Map<String, Long> reportRecord;
}