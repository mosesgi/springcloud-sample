package com.moses.cloud.sample.controller;

import com.moses.cloud.sample.entity.User;
import com.moses.cloud.sample.feign.UserFeignClient;
import com.moses.cloud.sample.feign.UserServiceExtend;
import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
//    @Autowired
//    private UserFeignClient userFeignClient;
    @Autowired
    private UserServiceExtend userServiceExtend;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

//    @GetMapping("/user/{id}")
//    public User findById(@PathVariable Long id) {
//        return this.userServiceExtend.findById(id);
//    }

    @GetMapping("/user-extend/{id}")
    public User findById2(@PathVariable Long id) {
        return userServiceExtend.getUser(id);
    }

    @GetMapping("/user/getIpAndPort")
    public String getIpAndPort() {
        return this.restTemplate.getForObject("http://ms-provider-user/getIpAndPort", String.class);
    }

    @GetMapping("/log-user-instance")
    public void logUserInstance() {
        ServiceInstance serviceInstance = this.loadBalancerClient.choose("ms-provider-user");
        // 打印当前选择的是哪个节点
        OrderController.LOGGER.info("{}:{}:{}", serviceInstance.getServiceId(), serviceInstance.getHost(), serviceInstance.getPort());
    }
}
