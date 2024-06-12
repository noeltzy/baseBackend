package com.tzy.basebackend.configration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Noel
 * Created on 2024/6/12
 * ClassName:AliyunOssConfigration
 * Package:com.tzy.basebackend.configration
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyahsConfiguration {
    //修改后
    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;
}
