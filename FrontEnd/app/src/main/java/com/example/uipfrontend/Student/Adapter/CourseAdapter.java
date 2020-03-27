package com.example.uipfrontend.Student.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;

import android.widget.TextView;

import com.example.uipfrontend.R;
import com.example.uipfrontend.Entity.Course;

import java.util.List;

public class CourseAdapter extends ArrayAdapter<Course> {
    private int resourceId;



    public CourseAdapter(Context context, int textViewResourceId, List<Course> fruits){
        super(context, textViewResourceId, fruits);
        this.resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Course course = getItem(position);
        View view = LayoutInflater.from(this.getContext()).inflate(resourceId, null);

        //ImageView courseImage = (ImageView)view.findViewById(R.id.courseImage);


        TextView courseName = (TextView)view.findViewById(R.id.coursename);
        TextView teacher = (TextView)view.findViewById(R.id.Teacher);
        TextView description = (TextView)view.findViewById(R.id.courseDescription);
        TextView score = (TextView)view.findViewById(R.id.RatingScore);


        //courseImage.setImageResource(course.getImageurl());
        courseName.setText(course.getName());
        teacher.setText(course.getTeacher());
        description.setText(course.getDescription());
        score.setText(String.valueOf(course.getScore()));


        return view;
    }
}