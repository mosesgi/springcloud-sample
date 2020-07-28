package com.moses.cloud.sample.feign;

import com.moses.cloud.sample.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="ms-provider-user")
public interface UserFeignClient {
    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    public User findById(@PathVariable("id") Long id);
}
