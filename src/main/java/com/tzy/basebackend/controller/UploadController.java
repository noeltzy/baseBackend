package com.tzy.basebackend.controller;

import com.tzy.basebackend.common.Result;
import com.tzy.basebackend.service.impl.UploadServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Noel
 * Created on 2024/6/12
 * ClassName:UploadController
 * Package:com.tzy.basebackend.controller
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class UploadController {
    @Resource
    private UploadServiceImpl uploadService;

    /**
     * 上传Img
     *
     * @param file img 文件
     * @return 文件oss地址
     */
    @PostMapping("/img")
    public Result<String> upload(MultipartFile file) throws IOException {
        return uploadService.uploadImg(file);
    }
}
