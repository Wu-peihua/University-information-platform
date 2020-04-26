package com.example.uipservice.service.Impl;

import com.example.uipservice.dao.RecruitMapper;
import com.example.uipservice.entity.Recruit;
import com.example.uipservice.service.RecruitService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class RecruitServiceImpl implements RecruitService {

    @Autowired
    RecruitMapper recruitMapper;

    @Override
    public boolean insertRecruit(Recruit recruit) {
        if(recruit.getUserId()!=null  ){
            try{
                int effectNum = recruitMapper.insert(recruit);
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
                int effectNum = recruitMapper.updateByPrimaryKey(recruit);
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
    public Map queryRecruitByUserId(Integer pageNum, Integer pageSize, Long userId) {
        Map recruitMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<Recruit> data = recruitMapper.queryRecruitByUserId(userId);
        recruitMap.put("recruit",data);  //分页获取的数据
        recruitMap.put("total",data.getTotal());       //总页数
        recruitMap.put("pageSize",data.getPageSize());     //每页大小
        return recruitMap;
    }

    @Override
    public Map queryRecruitByInfoId(Integer pageNum, Integer pageSize, Long infoId) {
        Map recruitMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<Recruit> data = recruitMapper.queryRecruitByInfoId(infoId);
        recruitMap.put("recruit",data);  //分页获取的数据
        recruitMap.put("total",data.getTotal());       //总页数
        recruitMap.put("pageSize",data.getPageSize());     //每页大小
        return recruitMap;    }

    @Override
    public Map queryRecruitByUniAndIns(Integer pageNum, Integer pageSize, Integer universityId, Integer instituteId) {
        Map recruitMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<Recruit> data = recruitMapper.queryRecruitByUniAndIns(universityId,instituteId);
        recruitMap.put("recruit",data);  //分页获取的数据
        recruitMap.put("total",data.getTotal());       //总页数
        recruitMap.put("pageSize",data.getPageSize());     //每页大小
        return recruitMap;    }

    @Override
    public Map queryRecruit(Integer pageNum, Integer pageSize) {
        Map recruitMap = new HashMap();
        PageHelper.startPage(pageNum,pageSize);
        Page<Recruit> data = recruitMapper.queryRecruit();
        recruitMap.put("recruit",data);  //分页获取的数据
        recruitMap.put("total",data.getTotal());       //总页数
        recruitMap.put("pageSize",data.getPageSize());     //每页大小
        return recruitMap;
    }
}
