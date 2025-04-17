package com.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.system.mapper")
public class MySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MySystemApplication.class, args);
    }

}
