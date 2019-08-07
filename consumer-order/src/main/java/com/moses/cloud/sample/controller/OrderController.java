package com.moses.cloud.sample.controller;

import com.moses.cloud.sample.entity.User;
import com.moses.cloud.sample.feign.UserFeignClient;
import com.moses.cloud.sample.feign.UserServiceExtend;
import com.netflix.discovery.converters.Auto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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

    @HystrixCommand(
            //https://github.com/Netflix/Hystrix/wiki/Configuration#CommandExecution
            commandProperties = {
                    // 熔断器在整个统计时间内是否开启的阀值
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
//                    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="4000"),
                    // 至少有3个请求才进行熔断错误比率计算
                    // 设置在一个滚动窗口中，打开断路器的最少请求数。
                    // 比如：如果值是20，在一个窗口内（比如10秒），收到19个请求，即使这19个请求都失败了，断路器也不会打开。
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "3"),
                    // 当出错率超过50%后熔断器启动
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    // 熔断器工作时间，超过这个时间，先放一个请求进去，成功的话就关闭熔断，失败就再等一段时间
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
                    // 统计滚动的时间窗口
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000")
            },
            //https://github.com/Netflix/Hystrix/wiki/Configuration#ThreadPool
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "15"),
                    // BlockingQueue的最大队列数，当设为-1，会使用SynchronousQueue，值为正时使用LinkedBlcokingQueue。
                    @HystrixProperty(name = "maxQueueSize", value = "15"),
                    // 设置存活时间，单位分钟。如果coreSize小于maximumSize，那么该属性控制一个线程从实用完成到被释放的时间.
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    //设置队列拒绝的阈值,即使maxQueueSize还没有达到
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "10"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000")
            },
            fallbackMethod = "errorContent"
    )
    @GetMapping("/user/{id}")
    public User findById(@PathVariable Long id) {
        LOGGER.info("=================请求用户中心接口==================");
        return restTemplate.getForObject("http://ms-provider-user/" + id, User.class);
    }

    public User errorContent(){
        User u = new User();
        u.setName("ErrorContent");
        return u;
    }

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
