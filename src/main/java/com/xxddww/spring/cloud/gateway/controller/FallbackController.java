package com.xxddww.spring.cloud.gateway.controller;

import com.xxddww.spring.cloud.gateway.base.ResponseData;
import com.xxddww.spring.cloud.gateway.enums.CommErrorEnums;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回调处理
 * <pre>
 *     1.路由正确，超过指定时间，回调该接口
 * </pre>
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("/test/service")
    public ResponseData baseService() {
        return ResponseData.fail(CommErrorEnums.CODE_0002.getCode(),"test-service服务不可用");
    }

    @RequestMapping("/demo/service")
    public ResponseData userService() {
        return ResponseData.fail(CommErrorEnums.CODE_0002.getCode(),"demo-service服务不可用");
    }

}