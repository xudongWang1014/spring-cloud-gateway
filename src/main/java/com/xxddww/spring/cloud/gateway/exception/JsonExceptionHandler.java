package com.xxddww.spring.cloud.gateway.exception;

import com.xxddww.spring.cloud.gateway.enums.HttpRtnCode;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义异常处理
 *
 * <pre>
 *     1.异常时用JSON代替HTML异常信息
 *     2.暂时只能处理自身项目有关的异常，下游业务项目的异常无法捕获，待解决
 * </pre>
 */
public class JsonExceptionHandler extends DefaultErrorWebExceptionHandler {


    public JsonExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ErrorProperties errorProperties, ApplicationContext applicationContext) {

        super(errorAttributes, resourceProperties, errorProperties, applicationContext);

    }

    /**
     * 获取异常属性
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {

        String code = HttpRtnCode.SERVER_ERROR.getCode() + "";

        Throwable error = super.getError(request);

        if (error instanceof org.springframework.cloud.gateway.support.NotFoundException) {

            code = HttpRtnCode.NOT_FOUND.getCode() + "";

        }

        return response(code, this.buildMessage(request, error));

    }

    /**
     * 指定响应处理方法为JSON处理的方法
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {

        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);

    }

    /**
     * 根据code获取对应的HttpStatus
     */
    @Override
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {

        Object code = errorAttributes.get("code");
        int statusCode = 404;
        if(code != null){
            statusCode = (int) code;
        }

        return HttpStatus.valueOf(statusCode);

    }

    /**
     * 构建异常信息
     */

    private String buildMessage(ServerRequest request, Throwable ex) {

        StringBuilder message = new StringBuilder("Failed to handle request [");

        message.append(request.methodName());

        message.append(" ");

        message.append(request.uri());

        message.append("]");

        if (ex != null) {

            message.append(": ");

            message.append(ex.getMessage());

        }

        return message.toString();

    }

    /**
     * 构建返回的JSON数据格式
     * <pre>
     *     1.跟父项目的ResponseData实体保持一致
     * </pre>
     */
    public static Map<String, Object> response(String errorCode, String errorMessage) {

        Map<String, Object> map = new HashMap<>();
        map.put("success", "false");
        map.put("errorCode", errorCode);
        map.put("message", errorMessage);
        map.put("dataObj", System.currentTimeMillis());

        return map;

    }


}