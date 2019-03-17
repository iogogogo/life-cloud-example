package com.iogogogo.consumer.feign;

import com.iogogogo.consumer.configure.FeignConfig;
import com.iogogogo.consumer.feign.fallback.ProducerFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * value = 服务提供者的 spring.application.name
 * fallback = 熔断降级处理类
 * configuration = 熔断降级配置
 * <p>
 * Created by tao.zeng on 2019-03-16.
 */
@Component
@FeignClient(value = "life-example-producer", fallback = ProducerFallback.class, configuration = FeignConfig.class)
public interface ProducerService {

    /**
     * 调用的远程方法路由地址需要和服务提供者的一致，并且不要使用GetMapping之类的简化方法
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/api/index", method = RequestMethod.GET)
    String index(@RequestParam("name") String name);

}
