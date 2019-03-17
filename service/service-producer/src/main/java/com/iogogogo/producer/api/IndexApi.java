package com.iogogogo.producer.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by tao.zeng on 2019-03-16.
 */
@RestController
@RequestMapping("/api")
public class IndexApi {


    @Value("${service.instance.name}")
    private String instanceName;

    @GetMapping("/index")
    public String index(String name) {
        return String.format("%s hello %s - %s", instanceName, name, UUID.randomUUID().toString());
    }
}
