package com.tzy.basebackend.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.tzy.basebackend.configration.AliyahsConfiguration;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @author Noel
 * Created on 2024/6/12
 * ClassName:AliyunOssUtils
 * Package:com.tzy.basebackend.utils
 */
@Slf4j
@Component
public class AliyunOssUtils {
    @Resource
    private AliyahsConfiguration aliyahsConfiguration;

    public String upload(MultipartFile file) throws IOException {

        String endpoint = aliyahsConfiguration.getEndpoint();
        String accessKeyId = aliyahsConfiguration.getAccessKeyId();
        String accessKeySecret = aliyahsConfiguration.getAccessKeySecret();
        String bucketName = aliyahsConfiguration.getBucketName();

        InputStream inputStream = file.getInputStream();
        String originalFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        //上传：
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        ossClient.putObject(bucketName, filename, inputStream);
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + filename;
        ossClient.shutdown();
        log.info(url);
        return url;
    }

}
