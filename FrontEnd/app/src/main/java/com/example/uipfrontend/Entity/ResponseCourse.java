package com.example.uipfrontend.Entity;

import java.util.List;

public class ResponseCourse {

    private Integer pageSize;
    private Integer pageNum;
    private Integer total;
    private List<Course> courseInfoList;

    public ResponseCourse(){}

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

    public List<Course> getCourseInfoList() {
        return courseInfoList;
    }

    public void setCourseInfoList(List<Course> courseInfoList) {
        this.courseInfoList = courseInfoList;
    }
}
