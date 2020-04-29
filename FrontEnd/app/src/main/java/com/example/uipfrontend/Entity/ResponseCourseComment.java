package com.example.uipfrontend.Entity;

import java.util.List;

public class ResponseCourseComment {

    private Integer pageSize;
    private Integer pageNum;
    private Integer total;
    private List<CourseComment> CourseEvaluationInfoList;

    public ResponseCourseComment(){}

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

    public List<CourseComment> getCourseEvaluationInfoList() {
        return CourseEvaluationInfoList;
    }

    public void setCourseCommentList(List<CourseComment> courseCommentList) {
        this.CourseEvaluationInfoList = courseCommentList;
    }
}
