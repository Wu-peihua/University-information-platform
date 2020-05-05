package com.example.uipservice.web;
import com.example.uipservice.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 *@user 图片上传及显示
 */
@RestController
@RequestMapping("/image")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @RequestMapping(value="/upload",method = RequestMethod.POST)
    public Map uploadApk(@RequestParam("image") MultipartFile multipartFiles, HttpServletRequest request, HttpServletResponse response) {
        Map modelMap = fileUploadService.upload(multipartFiles);
        return modelMap;
    }
}

