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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public RecruitInfo(){

    }

    public  RecruitInfo(Long infoId,String title,String contact,String content,Date infoDate,String portrait,String pictures,Integer universityId,Integer instituteId){
        this.infoId = infoId;
        this.title = title;
        this.contact = contact;
        this.content = content;
        this.infoDate = infoDate;
        this.portrait = portrait;
        this.pictures = pictures;
        this.instituteId = instituteId;
        this.universityId = universityId;

    }

    @Override
    public String toString(){
        return "RecruitInfo[ infoId:" + infoId + ",title:" + title + ",content:" + content + ",pictures:" + pictures + ",infoDate:" + infoDate  +
                ",portrait:" + portrait + ",institute::" + instituteId + ",universityId:" + universityId + ",contact:" + contact + "]";
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


    public Integer getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Integer instituteId) {
        this.instituteId = instituteId;
    }

    //组队信息Id
    private Long infoId;
    //组队信息标题
    private String title;
    //组队信息内容
    private String content;
    //组队信息图片
    private String pictures;
    //组队信息发布日期
    private Date infoDate;
    //信息发布人ID
    private Long userId;
    //组队信息发布人头像
    private String portrait;
    //组队信息所属学院Id
    private Integer instituteId;
    //组队信息所属学校id
    private Integer universityId;
    //发布者联系方式
    private String contact;






}
