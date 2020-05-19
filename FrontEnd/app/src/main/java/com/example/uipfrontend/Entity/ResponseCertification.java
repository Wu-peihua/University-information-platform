package com.example.uipfrontend.Entity;

import java.util.List;

public class ResponseCertification {
    private Integer pageSize;
    private Integer pageNum;
    private Integer total;
    private List<Certification> certificationList;
    private List<String> universityList;
    private List<String> instituteList;

    public  List<String> getUniversityList() {
        return universityList;
    }

    public void setUniversityList(List<String> universityList) {
        this.universityList=universityList;
    }

    public List<String> getInstituteList() {
        return instituteList;
    }

    public void setInstituteList(List<String> instituteList) {
        this.instituteList = instituteList;
    }

    public ResponseCertification(){}

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Certification> getCertificationList() {
        return certificationList;
    }

    public void setCertificationList(List<Certification> certificationList) {
        this.certificationList = certificationList;
    }

    @Override
    public String toString(){
        return "universityList:"+universityList.toString() + "instituteList:"+instituteList.toString();
    }

}
