package com.example.uipservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class StuCertification {
    private Long infoId;

    private Long userId;

    private String stuName;

    private String stuNumber;

    private Integer institudeId;

    private Integer universityId;

    private String stuCard;

    private Integer stuCer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date created;

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName == null ? null : stuName.trim();
    }

    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber == null ? null : stuNumber.trim();
    }

    public Integer getInstitudeId() {
        return institudeId;
    }

    public void setInstitudeId(Integer institudeId) {
        this.institudeId = institudeId;
    }

    public Integer getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Integer universityId) {
        this.universityId = universityId;
    }

    public String getStuCard() {
        return stuCard;
    }

    public void setStuCard(String stuCard) {
        this.stuCard = stuCard == null ? null : stuCard.trim();
    }

    public Integer getStuCer() {
        return stuCer;
    }

    public void setStuCer(Integer stuCer) {
        this.stuCer = stuCer;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}