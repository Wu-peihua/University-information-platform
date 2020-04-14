package com.example.uipfrontend.Entity;


import android.content.Intent;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/****
 * 组队信息类
 */
public class RecruitInfo implements Serializable {
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

    public int getType1() {
        return type1;
    }

    public void setType1(int type1) {
        this.type1 = type1;
    }

    public int getType2() {
        return type2;
    }

    public void setType2(int type2) {
        this.type2 = type2;
    }


    public  RecruitInfo(Integer infoId,String title,String contact,String content,Date infoDate,String userName,String portrait,String pictures,Integer type1,Integer type2){
        this.infoId = infoId;
        this.title = title;
        this.contact = contact;
        this.content = content;
        this.infoDate = infoDate;
        this.userName = userName;
        this.portrait = portrait;
        this.pictures = pictures;
        this.type1 = type1;
        this.type2 = type2;
    }
    {

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
    //学校选择id
    private Integer type1;
    //学科选择id
    private Integer type2;





}
