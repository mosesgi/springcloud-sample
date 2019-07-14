package com.moses.cloud.sample.controller;

import com.moses.cloud.sample.entity.User;
import com.moses.cloud.sample.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("eurekaRegistration")
    private Registration registration;

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = userRepository.getOne(id);
        return user;
    }

    @GetMapping("/getIpAndPort")
    public String findIpAndPort(){
        return registration.getHost() + ":" + registration.getPort();
    }
}
