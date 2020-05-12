package com.example.uipfrontend.Entity;

import java.util.List;

/*
    okhttp请求相应封装类
 */
public class ResponseRecruit {

    private Integer pageSize;
    private Integer pageNum;
    private Integer total;
    private List<RecruitInfo> recruitInfoList;
    private List<String> userNameList;
    private List<String> userPortraitList;


    public List<String> getUserPortraitList() {
        return userPortraitList;
    }

    public void setUserPortraitList(List<String> userPortraitList) {
        this.userPortraitList = userPortraitList;
    }




    public List<String> getUserNameList() {
        return userNameList;
    }

    public void setUserNameList(List<String> userNameList) {
        this.userNameList = userNameList;
    }


    public ResponseRecruit(){}

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<RecruitInfo> getRecruitInfoList() {
        return recruitInfoList;
    }

    public void setRecruitInfoList(List<RecruitInfo> recruitInfoList) {
        this.recruitInfoList = recruitInfoList;
    }

    @Override
    public String toString(){
        return "userNameList:"+userNameList.toString() + "recruitList:"+recruitInfoList.toString();
    }
}
