package com.moses.cloud.sample.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class ConfigClientController {

    @Value("${profile}")    //read from config server
    private String profile;

    @GetMapping("/config/profile")
    public String displayProfile(){
        return profile;
    }
}
