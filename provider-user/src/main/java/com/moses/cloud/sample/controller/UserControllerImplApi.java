package com.moses.cloud.sample.controller;

import com.moses.cloud.sample.entity.User;
import com.moses.cloud.sample.repository.UserRepository;
import com.moses.cloud.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImplApi implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(Long id) {
        return userRepository.getOne(id);
    }
}
