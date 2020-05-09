package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.CoursesMapper;
import com.example.uipservice.entity.Courses;
import com.example.uipservice.entity.UserInfo;
import com.example.uipservice.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CoursesMapper courseMapper;

    @Override
    public boolean insertCourse(Courses Course) {
        if(Course.getCourseName() != null){
            try{
                int effectNum = courseMapper.insert(Course);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，插入课程信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("插入课程的名为空！");
        }
    }

    @Override
    public boolean updateCourse(Courses Course) {
        if(Course.getCourseName() != null){
            try{
                int effectNum = courseMapper.updateByPrimaryKey(Course);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，更新课程信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("插入的课程名为空！");
        }
    }

    @Override
    public boolean deleteCourse(Long infoId) {
        if(infoId != null){
            try{
                int effectNum = courseMapper.deleteByPrimaryKey(infoId);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，删除课程信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("删除的课程信息的id为空！");
        }
    }



        @Override
        public Courses queryCourseByInfoId(Long infoId) {

            //PageHelper.startPage(pageNum,pageSize);
            //Page<Courses> data = courseMapper.queryCoursesByInfoId(infoId);
            //recruitMap.put("courseInfoList",data);  //分页获取的数据
            //recruitMap.put("total",data.getTotal());       //总页数
            //recruitMap.put("pageSize",data.getPageSize());     //每页大小
            //recruitMap.put("pageNum",pageNum);     //页数
            if(infoId != null){
                try {
                    Courses courses = courseMapper.queryCoursesByInfoId(infoId);
                    return courses;
                }catch (Exception e){
                    throw new RuntimeException("服务器操作错误: " + e.getMessage());
                }
            }else {
                throw new RuntimeException("课程id为空！");
            }

        }


    
        @Override
        public Map queryCourseByUniAndIns(Integer pageNum, Integer pageSize, Integer universityId, Integer instituteId) {
            Map recruitMap = new HashMap();
            PageHelper.startPage(pageNum,pageSize);
            Page<Courses> data = courseMapper.queryCoursesByUniAndIns(universityId, instituteId);
            recruitMap.put("courseInfoList",data);  //分页获取的数据
            recruitMap.put("total",data.getTotal());       //总页数
            recruitMap.put("pageSize",data.getPageSize());     //每页大小
            recruitMap.put("pageNum",pageNum);     //页数
    
            return recruitMap;
        }
    
        @Override
        public Map queryCourses(Integer pageNum, Integer pageSize) {
            Map recruitMap = new HashMap();
            PageHelper.startPage(pageNum,pageSize);
            Page<Courses> data = courseMapper.queryCourses();
            recruitMap.put("courseInfoList",data);  //分页获取的数据
            recruitMap.put("total",data.getTotal());       //总页数
            recruitMap.put("pageSize",data.getPageSize());     //每页大小
            recruitMap.put("pageNum",pageNum);     //页数
    
            return recruitMap;
        }
}
