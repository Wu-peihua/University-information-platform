package com.example.uipfrontend.Entity;

import android.content.Intent;

import java.io.Serializable;
import java.util.Date;

public class Course implements Serializable {

    /*private Integer courseid;//课程唯一标识
    private String coursename;//课程名称
    private String imageurl;//课程图片URL
    private Integer schoolid;//学校ID
    private Integer academyid;//学院ID
    private String description;//课程简介
    private String teachername;//教师名
    private double averagescore;//总评分
*/
    private Long infoId;

    private Integer userId;

    private Integer universityId;

    private Integer instituteId;

    private String courseName;

    private String teacherName;

    private String coursePicture;

    private Date infoDate;

    private String description;

    //修改平均分属性 float
    //private Integer averageScore;

    private Float averageScore;

    private Date created;

    /*public Course(Long courseID,String _name, String _teacher,String _description,Integer _score){
        this.infoId = courseID;
        this.courseName = _name;
        this.teacherName = _teacher;
        this.description = _description;
        this.averageScore = _score;
    }

     */

    public Course(Long courseID,String _name, String _teacher,String _description,Float _score){
        this.infoId = courseID;
        this.courseName = _name;
        this.teacherName = _teacher;
        this.description = _description;
        this.averageScore = _score;
    }

    public Long getCourseID(){
        return this.infoId;
    }

    public Integer getSchoolId() {
        return this.universityId;
    }

    public  Integer getAcademyId() {
        return this.instituteId;
    }

    public String getImageurl(){
        return this.coursePicture;
    }

    public String getName(){
        return this.courseName;
    }

    public  String getTeacher(){
        return this.teacherName;
    }
    public String getDescription(){
        return this.description;
    }

    public Float getScore(){
        return this.averageScore;
    }

    public void setCourseID(Long id) {
        this.infoId = id;
    }

    public void setCourseName(String name) {
        this.courseName = name;
    }

    public void setImageUrl(String Url) {
        this.coursePicture = Url;
    }

    public void setSchoolId(Integer schoolId){
        this.universityId =schoolId;
    }

    public void setAcademyId(Integer academyId){
        this.instituteId = academyId;

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public  void setTeacher(String teacher) {
        this.teacherName = teacher;
    }

    /*public  void  setScore(Integer score) {
        this.averageScore = score;
    }

     */
    public  void  setScore(Float score) {
        this.averageScore = score;
    }

    @Override
    public String toString(){
        return "CourseInfo[ courseId:" + infoId + ",courseName:" + courseName + ",imageUrl:" +coursePicture +  ",schoolId:" + universityId+
                "academyId:" + instituteId+",description:" + description  + ",teacher:" + teacherName  + ",score:" + averageScore +"]";
    }
}