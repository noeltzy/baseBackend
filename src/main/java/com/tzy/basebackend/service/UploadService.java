package com.tzy.basebackend.service;

import com.tzy.basebackend.common.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Noel
 * Created on 2024/6/12
 * ClassName:UploadService
 * Package:com.tzy.basebackend.service.impl
 */
public interface UploadService {
    Result<String> uploadImg(MultipartFile file) throws IOException;
}
