package com.iogogogo.producer;

import feign.Retryer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;

/**
 * Created by tao.zeng on 2019-03-16.
 */
@EnableHystrix
@EnableEurekaClient
@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @Bean
    public Retryer feignRetryer() {
        // new Retryer.Default(100,TimeUnit.SECONDS.toMillis(1),5);//默认是5次
        return Retryer.NEVER_RETRY;
    }
}
