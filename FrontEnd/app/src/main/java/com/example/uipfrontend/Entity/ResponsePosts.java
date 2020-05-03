package com.example.uipfrontend.Entity;

import java.util.List;

public class ResponsePosts {

    private Integer pageSize;
    private Integer pageNum;
    private Integer total;
    private List<ForumPosts> postsList;
    
    public ResponsePosts() {}
    
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

    public List<ForumPosts> getPostsList() {
        return postsList;
    }

    public void setPostsList(List<ForumPosts> postsList) {
        this.postsList = postsList;
    }
}
