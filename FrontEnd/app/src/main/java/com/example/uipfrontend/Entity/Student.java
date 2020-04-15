package com.example.uipfrontend.Entity;

import java.io.Serializable;

public class Student implements Serializable {
    private Integer userID;//唯一标识ID
    private String userimgl;//头像url
    //账号名和密码用于登录
    private String account;//账号名
    private String password;//密码
    private String userName;//昵称
    private int schoolID;//学校
    private int facultyID;//学院

    public Student(Integer userID,String account,String password,String userName,int schoolID,int facultyID){
        this.userID=userID;
        this.account=account;
        this.password=password;
        this.userName=userName;
        this.schoolID=schoolID;
        this.facultyID =facultyID;
    }

    public Integer getUserID(){
        return userID;
    }

    public String getAccount(){
        return account;
    }

    public String getUserName(){
        return userName;
    }

    public String getPassword(){
        return password;
    }

    public int getSchoolID(){
        return schoolID;
    }

    public int getAcademyIDID(){
        return facultyID;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setUserName(String userName){
        this.userName=userName;
    }

    public void setSchoolID(int schoolID){
        this.schoolID=schoolID;
    }

    public void setAcademyI(int academyID){
        this.facultyID =academyID;
    }

}