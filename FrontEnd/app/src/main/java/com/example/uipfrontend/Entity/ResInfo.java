package com.example.uipfrontend.Entity;

import java.io.Serializable;

public class ResInfo implements Serializable {
    private Long infoId;
    private String title;
    private String description;
    private String address;
    private Long userId;
    private String portrait;
    private String userName;
    private int subjectId;
    private int typeId;
    private int likeNumber = 0;
    private int reportNumber = 0;
    private int isAnonymous;
    private String created;

    public ResInfo() {
    }

    public ResInfo(Long infoId, String title, String description, String address, Long userId, String portrait, String userName,
                   int subjectId, int typeId, int likeNumber, int reportNumber, int isAnonymous, String created) {
        this.infoId = infoId;
        this.title = title;
        this.description = description;
        this.address = address;
        this.userId = userId;
        this.portrait = portrait;
        this.userName = userName;
        this.subjectId = subjectId;
        this.typeId = typeId;
        this.likeNumber = likeNumber;
        this.reportNumber = reportNumber;
        this.isAnonymous = isAnonymous;
        this.created = created;
    }

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
    }

    public int getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(int isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public boolean isAnonymous() {
        if (isAnonymous == 1)
            return true;
        return false;
    }

    public void setAnonymous(boolean isAnonymous) {
        if (isAnonymous)
            this.isAnonymous = 1;
        else
            this.isAnonymous = 0;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}