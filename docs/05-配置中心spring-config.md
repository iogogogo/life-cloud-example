# 配置中心

在系统架构中，和安全、日志、监控等非功能需求一样，配置管理也是一种非功能需求。配置中心是整个微服务基础架构体系中的一个组件，它的功能看上去并不起眼，无非就是简单配置的管理和存取，但它是整个微服务架构中不可或缺的一环。另外，配置中心如果真得用好了，它还能推动技术组织持续交付和DevOps文化转型。

## 目前一部分配置中心介绍

![img](images/config/config.png)



1. 阿里巴巴中间件部门很早就自研了配置中心Diamond，并且是开源的。Diamond对阿里系统的灵活稳定性发挥了至关重要的作用。开源版本的Diamond由于研发时间比较早，使用的技术比较老，功能也不够完善，目前社区不热已经不维护了。
2. Facebook内部也有一整套完善的配置管理体系，其中一个产品叫Gatekeeper，目前没有开源。
3. Netflix内部有大量的微服务，它的服务的稳定灵活性也重度依赖于配置中心。Netflix开源了它的配置中心的客户端，叫变色龙Archaius，比较可惜的是，Netflix没有开源它的配置中心的服务器端。
4. Apollo是携程框架部研发并开源的一款配置中心产品，企业级治理功能完善，目前社区比较火，在github上有超过10k星，在国内众多互联网公司有落地案例。**目前ITOA也是采用的Apollo配置中心**。
5. 百度之前也开源过一个叫Disconf的配置中心产品，作者是前百度资深工程师廖绮绮。在Apollo没有出来之前，Disconf在社区是比较火的，但是自从廖琦琦离开百度之后，他好像没有足够精力投入维护这个项目，目前社区活跃度已经大不如前。
6. 以及 Spring Cloud Config，和spring cloud生态是天然支持，当然，我个人觉得作为一个生产级别的配置中心，spring cloud config还是存在一定的缺陷的，比如一个可视化的管理界面，没有spring cloud bus等支持，无法做到热发布等等，但是我们还是简单介绍一下spring cloud config。**个人推荐生产使用携程Apollo**。



## Spring Cloud Config

Spring Cloud Config项目是一个解决分布式系统的配置管理方案。它包含了Client和Server两个部分，server提供配置文件的存储、以接口的形式将配置文件的内容提供出去，client通过接口获取数据、并依据此数据初始化自己的应用。Spring cloud使用git或svn存放配置文件，默认情况下使用git。

我们还是以之前的service-producer为基础，并且在项目根目录创建一个cloud-conf-repo文件夹用来存放配置，并且准备三个文件

```properties
# 开发环境
life-example-producer-dev.properties
# 测试环境
life-example-producer-test.properties
# 生产环境
life-example-producer-pro.properties
```

文件内容为分别为sharplook.instance=sharplook-[dev/test/pro]

### Server 端配置

#### pom 配置

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

#### 属性文件配置

```properties
spring.application.name=life-cloud-config
server.port=8899
eureka.instance.prefer-ip-address=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
# git仓库地址
spring.cloud.config.server.git.uri=
# git仓库地址下的相对地址 可以配置多个 用,分割。
spring.cloud.config.server.git.search-paths=
# git 仓库用户名
spring.cloud.config.server.git.username=
# git 仓库密码
spring.cloud.config.server.git.password=
# 如果有分支 可以在这里配置分支名称
spring.cloud.config.server.git.default-label=
```

#### 启动类配置

```java
package com.iogogogo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Created by tao.zeng on 2019-03-16.
 */
@EnableConfigServer
@EnableEurekaClient
@SpringBootApplication
public class ConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }
}
```

#### 测试

浏览器访问

```html
http://localhost:8899/life-example-producer/dev
http://localhost:8899/life-example-producer/test
http://localhost:8899/life-example-producer/pro
```

分别会按照我们的配置返回相应的数据，格式如下

```json
// 20190317102625
// http://localhost:8899/life-example-producer/dev

{
  "name": "life-example-producer",
  "profiles": [
    "dev"
  ],
  "label": null,
  "version": "131d288174b48601af4461dfde809913c87b91ca",
  "state": null,
  "propertySources": [
    {
      "name": "https://gitlab.eoitek.net/zengtao/life-cloud-example.git/cloud-conf-repo/life-example-producer-dev.properties",
      "source": {
        "sharplook.instance": "sharplook-dev"
      }
    }
  ]
}
```

仓库中的配置文件会被转换成web接口，访问可以参照以下的规则：

- /{application}/{profile}[/{label}]
- /{application}-{profile}.yml
- /{label}/{application}-{profile}.yml
- /{application}-{profile}.properties
- /{label}/{application}-{profile}.properties



### Client配置

client 主要是其他服务怎么去获取config中的配置信息，还是以service-producer为例

#### pom 配置

```xml
<!-- 添加config客户端依赖 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

#### 属性文件配置

application.properties无需修改，新建bootstrap.properties文件，用于配置spring cloud config的服务器信息

```properties
# 配置中心地址
spring.cloud.config.uri=http://localhost:8899/
# 使用哪个环境的配置
spring.cloud.config.profile=dev
# 当前服务的名称 和 spring.application.name对应即可
spring.cloud.config.name=${spring.application.name}
# 读取指定分支配置
spring.cloud.config.label=master
```

> 上面这些与spring-cloud相关的属性必须配置在bootstrap.properties中，config部分内容才能被正确加载。因为config的相关配置会先于application.properties，而bootstrap.properties的加载也是先于application.properties。

#### 启动类

- 无需变更

#### Controller

为了方便测试，还是使用接口访问形式，新建ConfigApi

```java
package com.iogogogo.producer.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tao.zeng on 2019-03-17.
 */
@RestController
@RequestMapping("/api/config")
public class ConfigApi {

    /**
     * 这里key 就是自定义在配置中心中的key
     */
    @Value("${sharplook.instance}")
    private String instance;

    @GetMapping("/")
    public String config() {
        return instance;
    }
}

```



#### 测试

启动service-producer服务，访问

```
http://localhost:8080/api/config/
```

返回

```
sharplook-dev
```

- 修改`spring.cloud.config.profile=pro`配置，重启服务后，访问接口，返现返回结果就是我们在config中的配置

- 修改配置中心的git中的配置，推送到git以后，再次请求服务，就能看到新的变更配置。



### 小结

1. 配置中心是微服务基础架构中不可或缺的核心组件，现代微服务架构和云原生环境，对应用配置管理提出了更高的要求。
2. 配置中心有众多的应用场景，配置中心+功能开关是DevOps最佳实践。用好配置中心，它能帮助技术组织实现持续交付和DevOps文化转型。
3. Spring Cloud Config相对来说还是达不到生产级别，目前来看携程开源的[Apollo](https://github.com/ctripcorp/apollo)配置中心，企业级功能完善，经过大规模生产验证，社区活跃度高，是开源配置中心产品的首选。



