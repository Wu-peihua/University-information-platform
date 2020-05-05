package com.example.uipservice.service.Impl;


import com.example.uipservice.service.FileUploadService;
import com.example.uipservice.util.UUIDUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${ImgPath}")
    private String realPath;// 保存的路径，本地磁盘中的一个文件夹

    @Value("${server.servlet.context-path}")
    private String path;// /demo

    @Value("${uploadImageBasePath}")
    private String basePath;

    @Override
    public Map upload(MultipartFile file) {
        Map modelMap = new HashMap();
        if(file == null || file.isEmpty()){
            modelMap.put("code", 400);
            modelMap.put("msg", "未选择文件");
            return  modelMap;
        }else{

            String newFilename = UUIDUtils.getUUID() + ".png";
            System.out.println("newFilename: " + newFilename);

            try {
                // 这里使用Apache的FileUtils方法来进行保存
                FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, newFilename));
                modelMap.put("code", 200);
                modelMap.put("msg", "上传成功");
                modelMap.put("filename", newFilename);
                modelMap.put("url", basePath + "/images/" + newFilename);
                System.out.println("上传文件的ulr：" + basePath + "/UIPImages/" + newFilename);
            } catch (IOException e) {
                modelMap.put("code", 500);
                System.out.println("文件上传失败");
                modelMap.put("msg", "文件上传失败");
                e.printStackTrace();
            }
            return modelMap;
        }

    }

    @Override
    public Boolean removeFile(String fileName) {
        return true;
    }
}
