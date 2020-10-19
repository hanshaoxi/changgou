package com.changgou.goods;

import entity.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author:zhangHao
 * @CreateDateTime : 2020-09-07  09 07 21:15
 * @Description:
 */
@SpringBootApplication
@MapperScan("com.changgou.goods.mapper")
@EnableEurekaClient
public class GoodsApplication {

    public static void main(String[] args) {

        SpringApplication.run(GoodsApplication.class , args);
    }

    @Bean
    public IdWorker createIdWorkder(){
        return  new IdWorker();
    }
}
