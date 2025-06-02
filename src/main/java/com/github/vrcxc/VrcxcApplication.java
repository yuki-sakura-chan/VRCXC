package com.github.vrcxc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.github.vrcxc.mapper")
public class VrcxcApplication {

    public static void main(String[] args) {
        SpringApplication.run(VrcxcApplication.class, args);
    }

}
