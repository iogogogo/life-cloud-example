package com.iogogogo.consumer.feign.fallback;

import com.iogogogo.consumer.feign.ProducerService;
import org.springframework.stereotype.Component;

/**
 * Created by tao.zeng on 2019-03-16.
 */
@Component
public class ProducerFallback implements ProducerService {

    @Override
    public String index(String name) {
        return String.format("life-example-producer 服务不可用 param:%s", name);
    }
}
