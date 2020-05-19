package com.example.uipfrontend.Entity;

import java.io.Serializable;

public class Certification implements Serializable {
    private Long infoId;
    private Long userId;
    private String stuName;
    private String stuNumber;
    private Integer institudeId;
    private Integer universityId;
    private String stuCard;
    private Integer stuCer;
    private String created;

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
        this.stuName = stuName;
    }

    public String getStuNumber() {
        return stuNumber;
    }

    public void setStuNumber(String stuNumber) {
        this.stuNumber = stuNumber;
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
        this.stuCard = stuCard;
    }

    public Integer getStuCer() {
        return stuCer;
    }

    public void setStuCer(Integer stuCer) {
        this.stuCer = stuCer;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString(){
        return "Certification[ infoId:" + infoId + ",userId:" + userId + ",stuName:" + stuName + ",stuNumber:" + stuNumber + ",stuCard:" + stuCard  +
                ",stuCer:" + stuCer + ",institute::" + institudeId + ",universityId:" + universityId  + "]";
    }



}
