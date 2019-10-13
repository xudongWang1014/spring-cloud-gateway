package com.xxddww.spring.cloud.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 路由器配置
 * <Pre>
 * 1.最终路由后的URL为：URI + 处理后的PATH
 * 2.URI只能是域名，不能带项目路径
 * 3.全局只能有一个路由配置
 * </Pre>
 */
@Configuration
public class RouteConfig {

    @Value("${test.service.url}")
    private String testServiceURL;
    @Value("${test.service.name}")
    private String testServiceName;

    @Value("${demo.service.url}")
    private String demoServiceURL;
    @Value("${demo.service.name}")
    private String demoServiceName;

    /**
     * 路由入口
     */
    @Bean
    public RouteLocator platformRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // test-service路由入口
                .route(r -> r.path("/gateway/test/**")
                        .filters(f -> f.stripPrefix(1)
                        .rewritePath("/test/(?<segment>.*)", "/"+ testServiceName + "/$\\{segment}")
                        .hystrix(config -> config.setName("testecmd").setFallbackUri("forward:/fallback/test/service")))
                        .uri(testServiceURL))

                // demo-service路由入口
                .route(r -> r.path("/gateway/demo/**")
                        .filters(f -> f.stripPrefix(1)
                        .rewritePath("/demo/(?<segment>.*)", "/"+ demoServiceName + "/$\\{segment}")
                        .hystrix(config -> config.setName("democmd").setFallbackUri("forward:/fallback/demo/service")))
                        .uri(demoServiceURL))

                .build();
    }

}

