package com.iogogogo.consumer.api;

import com.iogogogo.consumer.feign.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tao.zeng on 2019-03-16.
 */
@RestController
@RequestMapping("/api/consumer")
public class IndexApi {

    @Autowired
    private ProducerService producerService;

    @RequestMapping("/index")
    public String index(String name) {
        return producerService.index(name);
    }
}
