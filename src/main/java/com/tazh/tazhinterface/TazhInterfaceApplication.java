package com.tazh.tazhinterface;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
@SpringBootApplication
@MapperScan("com.tazh.tazhinterface.mapper")
public class TazhInterfaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TazhInterfaceApplication.class, args);
    }

}
