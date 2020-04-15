package com.example.uipfrontend.Entity;

import java.io.Serializable;

public class Course implements Serializable {

    private Integer courseID;//课程唯一标识
    private String courseName;//课程名称
    private String imageUrl;//课程图片URL
    private Integer schoolId;//学校ID
    private Integer academyId;//学院ID
    private String description;//课程简介
    private String teacher;//教师名
    private double score;//总评分

    public Course(Integer courseID,String _name, String _teacher,String _description,double _score){
        this.courseID = courseID;
        this.courseName = _name;
        this.teacher = _teacher;
        this.description = _description;
        this.score = _score;
    }

    public Integer getCourseID(){
        return this.courseID;
    }

    public Integer getSchoolId() {
        return this.schoolId;
    }

    public  Integer getAcademyId() {
        return this.academyId;
    }

    public String getImageurl(){
        return this.imageUrl;
    }

    public String getName(){
        return this.courseName;
    }

    public  String getTeacher(){
        return this.teacher;
    }
    public String getDescription(){
        return this.description;
    }

    public double getScore(){
        return this.score;
    }

    public void setCourseID(Integer id) {
        this.courseID = id;
    }

    public void setCourseName(String name) {
        this.courseName = name;
    }

    public void setImageUrl(String Url) {
        this.imageUrl = Url;
    }

    public void setSchoolId(Integer schoolId){
        this.schoolId =schoolId;
    }

    public void setAcademyId(Integer academyId){
        this.academyId = academyId;

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public  void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public  void  setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString(){
        return "CourseInfo[ courseId:" + courseID + ",courseName:" + courseName + ",imageUrl:" +imageUrl +  ",schoolId:" + schoolId+
                "academyId:" + academyId+",description:" + description  + ",teacher:" + teacher  + ",score:" + score +"]";
    }
}