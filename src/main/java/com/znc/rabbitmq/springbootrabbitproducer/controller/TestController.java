package com.znc.rabbitmq.springbootrabbitproducer.controller;

import com.znc.rabbitmq.springbootrabbitproducer.component.RabbitSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhunc
 * @Date 2022/5/14 18:03
 */
@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    RabbitSender rabbitSender;
    @RequestMapping("send")
    public void test(@RequestParam String msg) throws  Exception{
        Map<String, Object> map = new HashMap<>();
        map.put("attr1", "hello");
        map.put("attr2", "world");
        rabbitSender.send(msg, map);
        Thread.sleep(10000);
    }
}
