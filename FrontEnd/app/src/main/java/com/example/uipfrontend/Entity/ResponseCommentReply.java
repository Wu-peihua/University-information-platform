package com.example.uipfrontend.Entity;

import java.util.List;

public class ResponseCommentReply {
    
    private Integer pageSize;
    private Integer pageNum;
    private Integer total;
    private List<PostComment> replyList;

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

    public List<PostComment> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<PostComment> replyList) {
        this.replyList = replyList;
    }
}
