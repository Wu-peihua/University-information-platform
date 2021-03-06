package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.RecruitMapper;
import com.example.uipservice.dao.UserInfoMapper;
import com.example.uipservice.entity.Recruit;
import com.example.uipservice.service.RecruitService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RecruitServiceImpl implements RecruitService {

    @Autowired
    RecruitMapper recruitMapper;
    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public boolean insertRecruit(Recruit recruit) {
        if(recruit.getUserId()!=null  ){
            try{
                int effectNum = recruitMapper.insertSelective(recruit);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，发布组队信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("发布组队信息用户的id为空！");
        }
    }

    @Override
    public boolean updateRecruit(Recruit recruit) {
        if(recruit.getUserId()!=null  ){
            try{
                int effectNum = recruitMapper.updateByPrimaryKeySelective(recruit);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，更新组队信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("更新组队信息用户的id为空！");
        }
    }

    @Override
    public boolean deleteRecruit(Long infoId) {
        if(infoId !=null  ){
            try{
                int effectNum = recruitMapper.deleteByPrimaryKey(infoId);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，删除组队信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("删除组队信息用户的id为空！");
        }
    }

    @Override
    public boolean modifyRecruitReportNumber(Long infoId) {
        if(infoId !=null  ){
            try{
                int effectNum = recruitMapper.modifyReportNumberById(infoId);
                if(effectNum > 0){
                    return true;
                }else{
                    throw new RuntimeException("服务器错误，修改组队信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException(e.getMessage());
            }
        }else{
            throw new RuntimeException("修改组队信息用户的id为空！");
        }
    }

    @Override
    public Map queryRecruitByUserId(Integer pageNum, Integer pageSize, Long userId) {
        Map recruitMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<Recruit> data = recruitMapper.queryRecruitByUserId(userId);
        List<String> userNameList = new ArrayList<>();
        for(Recruit recruit: data){
            userNameList.add(userInfoMapper.selectByPrimaryKey(recruit.getUserId()).getUserName());
        }
        List<String> userPortraitList = new ArrayList<>();
        for(Recruit recruit: data){
            userPortraitList.add(userInfoMapper.selectByPrimaryKey(recruit.getUserId()).getPortrait());
        }
        recruitMap.put("userPortraitList",userPortraitList);
        recruitMap.put("userNameList",userNameList);
        recruitMap.put("recruitInfoList",data);  //分页获取的数据
        recruitMap.put("total",data.getTotal());       //总页数
        recruitMap.put("pageSize",data.getPageSize());     //每页大小
        recruitMap.put("pageNum",pageNum);     //页数

        return recruitMap;
    }

    @Override
    public Map queryRecruitByInfoId(Integer pageNum, Integer pageSize, Long infoId) {
        Map recruitMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<Recruit> data = recruitMapper.queryRecruitByInfoId(infoId);
        recruitMap.put("recruitInfoList",data);  //分页获取的数据
        recruitMap.put("total",data.getTotal());       //总页数
        recruitMap.put("pageSize",data.getPageSize());     //每页大小
        recruitMap.put("pageNum",pageNum);     //页数

        return recruitMap;
    }

    @Override
    public Map queryRecruitByUniAndIns(Integer pageNum, Integer pageSize, Integer universityId, Integer instituteId) {
        Map recruitMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<Recruit> data = recruitMapper.queryRecruitByUniAndIns(universityId,instituteId);
        List<String> userNameList = new ArrayList<>();
        for(Recruit recruit: data){
            userNameList.add(userInfoMapper.selectByPrimaryKey(recruit.getUserId()).getUserName());
        }
        List<String> userPortraitList = new ArrayList<>();
        for(Recruit recruit: data){
            userPortraitList.add(userInfoMapper.selectByPrimaryKey(recruit.getUserId()).getPortrait());
        }
        recruitMap.put("userPortraitList",userPortraitList);
        recruitMap.put("userNameList",userNameList);
        recruitMap.put("recruitInfoList",data);  //分页获取的数据
        recruitMap.put("total",data.getTotal());       //总页数
        recruitMap.put("pageSize",data.getPageSize());     //每页大小
        recruitMap.put("pageNum",pageNum);     //页数

        return recruitMap;
    }

    @Override
    public Map queryRecruit(Integer pageNum, Integer pageSize) {
        Map recruitMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<Recruit> data = recruitMapper.queryRecruit();
        List<String> userNameList = new ArrayList<>();
        for(Recruit recruit: data){
            userNameList.add(userInfoMapper.selectByPrimaryKey(recruit.getUserId()).getUserName());
        }
        List<String> userPortraitList = new ArrayList<>();
        for(Recruit recruit: data){
            userPortraitList.add(userInfoMapper.selectByPrimaryKey(recruit.getUserId()).getPortrait());
        }
        recruitMap.put("userPortraitList",userPortraitList);
        recruitMap.put("userNameList",userNameList);
        recruitMap.put("recruitInfoList",data);  //分页获取的数据
        recruitMap.put("total",data.getTotal());       //总页数
        recruitMap.put("pageSize",data.getPageSize());     //每页大小
        recruitMap.put("pageNum",pageNum);     //页数


        return recruitMap;
    }
    @Override
    public Map queryRecruitByUniAndInsReport(Integer pageNum, Integer pageSize, Integer universityId, Integer instituteId) {
        Map recruitMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<Recruit> data = recruitMapper.queryRecruitByUniAndInsReport(universityId,instituteId);
        List<String> userNameList = new ArrayList<>();
        for(Recruit recruit: data){
            userNameList.add(userInfoMapper.selectByPrimaryKey(recruit.getUserId()).getUserName());
        }
        List<String> userPortraitList = new ArrayList<>();
        for(Recruit recruit: data){
            userPortraitList.add(userInfoMapper.selectByPrimaryKey(recruit.getUserId()).getPortrait());
        }
        recruitMap.put("userPortraitList",userPortraitList);
        recruitMap.put("userNameList",userNameList);
        recruitMap.put("recruitInfoList",data);  //分页获取的数据
        recruitMap.put("total",data.getTotal());       //总页数
        recruitMap.put("pageSize",data.getPageSize());     //每页大小
        recruitMap.put("pageNum",pageNum);     //页数

        return recruitMap;
    }

    @Override
    public Map queryRecruitReport(Integer pageNum, Integer pageSize) {
        Map recruitMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<Recruit> data = recruitMapper.queryRecruitReport();
        List<String> userNameList = new ArrayList<>();
        for(Recruit recruit: data){
            userNameList.add(userInfoMapper.selectByPrimaryKey(recruit.getUserId()).getUserName());
        }
        List<String> userPortraitList = new ArrayList<>();
        for(Recruit recruit: data){
            userPortraitList.add(userInfoMapper.selectByPrimaryKey(recruit.getUserId()).getPortrait());
        }
        recruitMap.put("userPortraitList",userPortraitList);
        recruitMap.put("userNameList",userNameList);
        recruitMap.put("recruitInfoList",data);  //分页获取的数据
        recruitMap.put("total",data.getTotal());       //总页数
        recruitMap.put("pageSize",data.getPageSize());     //每页大小
        recruitMap.put("pageNum",pageNum);     //页数


        return recruitMap;
    }
}
