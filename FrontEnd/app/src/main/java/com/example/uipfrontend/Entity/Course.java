package com.example.uipfrontend.Entity;

public class Course {
    private String name;
    private String imageurl;
    private String schoolId;
    private String academyId;
    private String description;
    private String teacher;
    private double score;

    public Course(String _name, String _teacher,String _description,double _score){
        this.name = _name;
        this.teacher = _teacher;
        this.description = _description;
        this.score = _score;
    }

    public String getImageurl(){
        return this.imageurl;
    }

    public String getName(){
        return this.name;
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
}