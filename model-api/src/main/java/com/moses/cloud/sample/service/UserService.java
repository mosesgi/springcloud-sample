package com.moses.cloud.sample.service;

import com.moses.cloud.sample.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserService {
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable(value="id") Long id);
}
