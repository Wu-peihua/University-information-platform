package com.example.uipfrontend.Entity;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Date;

public class UserRecord implements Serializable {
    private Long infoId;
    private Long userId;
    private Long toId;
    private int tag;
    private int type;
    private Date created;

    public Long getInfoId() {
        return infoId;
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

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @NotNull
    @Override
    public String toString(){
        return "UserRecord:[infoId:" + infoId + ",userId:" + userId + ",toId:" + toId + ",tag:"+tag + ",type:"+type +",created:" + created +"]";
    }
}