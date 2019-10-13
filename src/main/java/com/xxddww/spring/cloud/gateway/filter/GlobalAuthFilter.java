package com.xxddww.spring.cloud.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.xxddww.spring.cloud.gateway.base.ResponseData;
import com.xxddww.spring.cloud.gateway.enums.CommErrorEnums;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 全局鉴权过滤器
 */
@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GlobalAuthFilter.class);

    /*
     * 过滤器存在优先级，order越大，优先级越低
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //请求之前

        //TODO 这里可以做权限校验，请求过滤，参数修改之类的逻辑

        boolean authFlag = false;

        if(!authFlag){
            return fail(exchange, chain, CommErrorEnums.CODE_0001);
        }

        return success(exchange, chain);
    }
    /**
     *  鉴权不通过返回
     */
    private Mono<Void> fail(ServerWebExchange exchange, GatewayFilterChain chain, CommErrorEnums errorEnum){

        ServerHttpResponse response = exchange.getResponse();
        byte[] bits = this.makeResData(errorEnum.getCode(), errorEnum.getMessage()).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type","application/json; charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    /**
     *  鉴权通过
     */
    private Mono<Void> success(ServerWebExchange exchange, GatewayFilterChain chain){
       return chain.filter(exchange);
    }

    /**
     * 组装相应报文
     */
    private String makeResData(String errorCode, String msg){

        ResponseData responseData = new ResponseData();
        responseData.setErrorCode(StringUtils.isBlank(errorCode) ?  "404" : errorCode);
        responseData.setMessage(msg);
        responseData.setSuccess(Boolean.FALSE);

        return JSONObject.toJSONString(responseData);
    }

}
