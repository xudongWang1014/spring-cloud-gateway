package com.xxddww.spring.cloud.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.xxddww.spring.cloud.gateway.base.ResponseData;
import org.apache.commons.lang3.ArrayUtils;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 异常处理过滤器
 */
@Component
public class ExceptionHandlerFilter implements GlobalFilter, Ordered {


    @Override
    public int getOrder() {
        //WRITE_RESPONSE_FILTER 之前执行
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return success(exchange, chain);
    }
    /**
     *  重写返回信息
     */
    private Mono<Void> success(ServerWebExchange exchange, GatewayFilterChain chain){
        ServerHttpResponse originalResponse = exchange.getResponse();

        ServerHttpResponse response = exchange.getResponse();
        DataBufferFactory bufferFactory = response.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (!HttpStatus.OK.equals(getStatusCode()) && body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    //封装后的数据
                    StringBuffer responseContent = new StringBuffer();
                    //API返回原始数据
                    List<Byte> rawResponseByteList = new ArrayList<>();
                    Flux<DataBuffer> dataBufferFlux = fluxBody.buffer().map(dataBuffers -> {
                        // Gateway 使用WebFlow数据为异步传输，需要等待所有数据返回此数据可能为空
                        dataBuffers.forEach(dataBuffer -> {
                            byte[] contentBytes = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(contentBytes);
                            List<Byte> contentByteList = Arrays.asList(ArrayUtils.toObject(contentBytes));
                            rawResponseByteList.addAll(contentByteList);
                            // 释放掉内存
                            DataBufferUtils.release(dataBuffer);
                        });
                        //数据转换成字节，封装转换
                        Byte[] rawResponseBytes = rawResponseByteList.toArray(new Byte[rawResponseByteList.size()]);
                        responseContent.append(new String(ArrayUtils.toPrimitive(rawResponseBytes), Charset.defaultCharset()));

                        ResponseData responseData = new ResponseData();
                        responseData.setErrorCode(response.getStatusCode().toString());
                        responseData.setDataObj(responseContent.toString());

                        String resStr = JSONObject.toJSONString(responseData);
                        byte[] uppedContent = new String(resStr.getBytes(), Charset.forName("UTF-8")).getBytes();
                        originalResponse.getHeaders().setContentLength(uppedContent.length);

                        return bufferFactory.wrap(uppedContent);
                    });
                    return super.writeWith(dataBufferFlux);
                }
                return super.writeWith(body);
            }
        };

        // replace response with decorator
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

}
