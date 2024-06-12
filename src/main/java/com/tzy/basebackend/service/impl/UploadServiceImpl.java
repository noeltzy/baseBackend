package com.tzy.basebackend.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.tzy.basebackend.common.BusinessException;
import com.tzy.basebackend.common.Result;
import com.tzy.basebackend.service.UploadService;
import com.tzy.basebackend.utils.AliyunOssUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.tzy.basebackend.constant.SystemConstant.IMAGE_TYPE;

/**
 * @author Noel
 * Created on 2024/6/12
 * ClassName:UploadServiceImpl
 * Package:com.tzy.basebackend.service.impl
 */
@Slf4j
@Service
public class UploadServiceImpl implements UploadService {
    @Resource
    private AliyunOssUtils aliyunOssUtils;

    @Override
    public Result<String> uploadImg(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            BusinessException.error("上传文件为空");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ObjectUtil.equals(contentType, IMAGE_TYPE)) {
            BusinessException.error("文件非图片");
        }
        String upload = "";
        try {
            upload = aliyunOssUtils.upload(file);
        } catch (IOException e) {
            BusinessException.error("文件上传错误");
        }
        return Result.ok(upload);
    }
}
