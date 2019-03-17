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
