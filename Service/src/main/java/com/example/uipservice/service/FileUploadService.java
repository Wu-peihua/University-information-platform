package com.example.uipservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileUploadService {

    Map upload(MultipartFile file);

    Boolean removeFile(String fileName);
}
