package com.example.uipfrontend.Entity;

import java.util.List;

public class ResponseResource {
    private Integer pageSize;
    private Integer pageNum;
    private Integer total;
    private List<ResInfo> resInfoList;

    public ResponseResource() {
    }

    public ResponseResource(Integer pageSize, Integer pageNum, Integer total, List<ResInfo> resInfoList) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.total = total;
        this.resInfoList = resInfoList;
    }

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

    public List<ResInfo> getResInfoList() {
        return resInfoList;
    }

    public void setResInfoList(List<ResInfo> ResInfoList) {
        this.resInfoList = ResInfoList;
    }
}
