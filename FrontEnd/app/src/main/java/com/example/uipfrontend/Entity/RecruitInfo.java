package com.example.uipfrontend.Entity;


import android.content.Intent;

import java.util.Date;

/****
 * 组队信息类
 */
public class RecruitInfo {
    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public Date getInfoDate() {
        return infoDate;
    }

    public void setInfoDate(Date infoDate) {
        this.infoDate = infoDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Integer universityId) {
        this.universityId = universityId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    @Override
    public String toString(){
        return "RecruitInfo[ infoId:" + infoId + ",title:" + title + ",content:" + content + ",pictures:" + pictures + ",infoDate:" + infoDate + ",userName:" + userName +
                ",portrait:" + portrait + ",subjected:" + subjectId + ",universityId:" + universityId + ",contact:" + contact + "]";
    }

    //组队信息Id
    private Integer infoId;
    //组队信息标题
    private String title;
    //组队信息内容
    private String content;
    //组队信息图片
    private String pictures;
    //组队信息发布日期
    private Date infoDate;
    //组队信息发布人姓名
    private String userName;
    //组队信息发布人头像
    private String portrait;
    //组队信息所属科目Id
    private Integer subjectId;
    //组队信息所属学校id
    private Integer universityId;
    //发布者联系方式
    private String contact;






}
