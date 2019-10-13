package com.xxddww.spring.cloud.gateway.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;

/**
 * 程序启动类
 */
@SpringBootApplication
@EnableHystrix
@ComponentScan(basePackages = {"com.xxddww.*.*"})
public class GatewayApplication {

    private final static Logger logger = LoggerFactory.getLogger(GatewayApplication.class);


    public static void main(String[] args){

        SpringApplication.run(GatewayApplication.class, args);

        logger.info("【GatewayApplication】启动完毕");
    }

}
