package com.tzy.basebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//开启支持javaWeb三大组件
//@ServletComponentScan
@SpringBootApplication
//开启mybatis支持
@MapperScan("com.tzy.basebackend.mapper")
public class BaseBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseBackendApplication.class, args);
    }

}
