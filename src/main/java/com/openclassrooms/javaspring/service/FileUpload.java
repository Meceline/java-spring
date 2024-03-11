package com.openclassrooms.javaspring.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface  FileUpload {
    String uploadFile(MultipartFile multipartfile) throws IOException;
}
