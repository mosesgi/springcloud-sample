package com.moses.cloud.sample.feign;

import com.moses.cloud.sample.service.UserService;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="ms-provider-user")
public interface UserServiceExtend extends UserService {
}
