package com.moses.cloud.sample.feign;

import com.moses.cloud.sample.entity.User;
import com.moses.cloud.sample.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

//fallback属性用于指定回退类
@FeignClient(name="ms-provider-user", fallback = UserServiceFallback.class)
public interface UserServiceExtend extends UserService {
}

@Component
class UserServiceFallback implements UserServiceExtend{

    @Override
    public User getUser(Long id) {
        User u = new User();
        u.setId(-1L);
        u.setUsername("Default User");
        return u;
    }
}