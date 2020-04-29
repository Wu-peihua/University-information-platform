package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.CourseEvaluationMapper;
import com.example.uipservice.entity.CourseEvaluation;
import com.example.uipservice.service.CourseEvaluationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class CourseEvaluationServiceImpl implements CourseEvaluationService {

    @Autowired
    CourseEvaluationMapper courseEvaluationMapper;

    @Override
    public boolean insertCourseEvaluation(CourseEvaluation CourseEvaluation) {
        if(CourseEvaluation.getCommentatorId()!=null  ){
            try{
                int effectNum = courseEvaluationMapper.insert(CourseEvaluation);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，发布课程评论信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("发布课程评论信息用户的id为空！");
        }
    }

    @Override
    public boolean updateCourseEvaluation(CourseEvaluation CourseEvaluation) {
        if(CourseEvaluation.getCommentatorId()!=null  ){
            try{
                int effectNum = courseEvaluationMapper.updateByPrimaryKey(CourseEvaluation);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，更新课程评论信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("更新课程评论信息用户的id为空！");
        }
    }

    @Override
    public boolean deleteCourseEvaluation(Long infoId) {
        if(infoId !=null  ){
            try{
                int effectNum = courseEvaluationMapper.deleteByPrimaryKey(infoId);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，删除组队信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("删除课程评论信息用户的id为空！");
        }
    }

    @Override
    public Map queryCourseEvaluationByUserId(Integer pageNum, Integer pageSize, Long userId) {
        Map CourseEvaluationMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<CourseEvaluation> data = courseEvaluationMapper.queryCourseEvaluationByUserId(userId);
        CourseEvaluationMap.put("CourseEvaluationInfoList",data);  //分页获取的数据
        CourseEvaluationMap.put("total",data.getTotal());       //总页数
        CourseEvaluationMap.put("pageSize",data.getPageSize());     //每页大小
        CourseEvaluationMap.put("pageNum",pageNum);     //页数

        return CourseEvaluationMap;
    }

    @Override
    public Map queryCourseEvaluationByInfoId(Integer pageNum, Integer pageSize, Long infoId) {
        Map CourseEvaluationMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<CourseEvaluation> data = courseEvaluationMapper.queryCourseEvaluationByInfoId(infoId);
        CourseEvaluationMap.put("CourseEvaluationInfoList",data);  //分页获取的数据
        CourseEvaluationMap.put("total",data.getTotal());       //总页数
        CourseEvaluationMap.put("pageSize",data.getPageSize());     //每页大小
        CourseEvaluationMap.put("pageNum",pageNum);     //页数

        return CourseEvaluationMap;    }


    @Override
    public Map queryCourseEvaluation(Integer pageNum, Integer pageSize) {
        Map CourseEvaluationMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<CourseEvaluation> data = courseEvaluationMapper.queryCourseEvaluation();
        CourseEvaluationMap.put("CourseEvaluationInfoList",data);  //分页获取的数据
        CourseEvaluationMap.put("total",data.getTotal());       //总页数
        CourseEvaluationMap.put("pageSize",data.getPageSize());     //每页大小
        CourseEvaluationMap.put("pageNum",pageNum);     //页数

        return CourseEvaluationMap;
    }

    @Override
    public Map queryCourseEvaluationByCourseId(Integer pageNum, Integer pageSize, Long courseId) {
        Map CourseEvaluationMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<CourseEvaluation> data = courseEvaluationMapper.queryCourseEvaluationByCourseId(courseId);
        CourseEvaluationMap.put("CourseEvaluationInfoList",data);  //分页获取的数据
        CourseEvaluationMap.put("total",data.getTotal());       //总页数
        CourseEvaluationMap.put("pageSize",data.getPageSize());     //每页大小
        CourseEvaluationMap.put("pageNum",pageNum);     //页数

        return CourseEvaluationMap;
    }
}
