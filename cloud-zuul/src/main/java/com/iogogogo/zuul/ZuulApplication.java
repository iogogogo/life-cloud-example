package com.iogogogo.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @EnableZuulServer 表示一个网关服务
 * @EnableZuulProxy 表示支持路由网关
 * <p>
 * Created by tao.zeng on 2019-03-16.
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
