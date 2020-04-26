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
}
