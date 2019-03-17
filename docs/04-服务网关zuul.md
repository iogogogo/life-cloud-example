# 服务网关 Zuul

Eureka用于服务的注册于发现，Feign支持服务的调用以及均衡负载，Hystrix处理服务的熔断防止故障扩散。

但是外部的应用如何来访问内部各种各样的微服务呢？在微服务架构中，后端服务往往不直接开放给调用端，而是通过一个API网关根据请求的url，路由到相应的服务。当添加API网关后，在第三方调用端和服务提供方之间就创建了一面墙，这面墙直接与调用方通信进行权限控制，后将请求均衡分发给后台服务端。

## Spring Cloud Zuul

### 添加依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>life-cloud-example</artifactId>
        <groupId>com.iogogogo</groupId>
        <version>0.0.1</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>cloud-zuul</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
    </dependencies>

</project>
```



### 配置文件

```properties
spring.application.name=life-cloud-zuul
server.port=8888
eureka.instance.prefer-ip-address=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# 表示访问iogogogo 都会跳转到 https://iogogogo.github.io/
zuul.routes.iogogogo.path=/iogogogo/*
zuul.routes.iogogogo.url=https://iogogogo.github.io/
```



### 启动类

```java
package com.iogogogo.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;

/**
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
```

依次启动cloud-eureka、cloud-zuul

### 微服务整合

通过url映射的方式来实现zuul的转发有局限性，比如每增加一个服务就需要配置一条内容，另外后端的服务如果是动态来提供，就不能采用这种方案来配置了。实际上在实现微服务架构时，服务名与服务实例地址的关系在eureka server中已经存在了，所以只需要将Zuul注册到eureka server上去发现其他服务，就可以实现对serviceId的映射。

#### 配置文件

```properties
spring.application.name=life-cloud-zuul
server.port=8888
eureka.instance.prefer-ip-address=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# 表示访问iogogogo 都会跳转到 https://iogogogo.github.io/
zuul.routes.iogogogo.path=/iogogogo/**
zuul.routes.iogogogo.url=https://iogogogo.github.io/

# 添加微服务路由地址
zuul.routes.producer.path=/producer/**
zuul.routes.producer.service-id=life-example-producer
```

#### 启动服务提供者

```shell
java -jar service-producer-0.0.1.jar --server.port=8080 --service.instance.name=这是服务器1

java -jar service-producer-0.0.1.jar --server.port=8081 --service.instance.name=这是服务器2

java -jar service-producer-0.0.1.jar --server.port=8082 --service.instance.name=这是服务器3
```

访问

```html
http://localhost:8888/producer/api/index?name=小花脸
```

会发现自动负载均衡，将每个请求分发到不同的服务，至此，整个zuul和微服务整合也就完成了。



## spring cloud gateway

[猿天地spring cloud gateway系列教程](http://cxytiandi.com/article)

