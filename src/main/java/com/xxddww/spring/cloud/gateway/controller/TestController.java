package com.xxddww.spring.cloud.gateway.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);


    @RequestMapping(value = "/hystrix.do")
    public Object hystrix() throws Exception {

        logger.info("*********  headKey  ***********");

        System.out.println("************   hystrix    ************");

        return "public --- hystrix测试成功";
    }

}


