package com.moses.cloud.sample.controller;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class HystrixDemoController {
    private final static Random random = new Random();

    @GetMapping("/hello-world-2")
    public String helloWorld2() throws Exception{
        return new HelloWorldCommand().execute();
    }

    /**
     * 编程方式构建
     */
    private class HelloWorldCommand extends com.netflix.hystrix.HystrixCommand<String>{

        protected HelloWorldCommand() {
            super(HystrixCommandGroupKey.Factory.asKey("HelloWorld"), 100);
        }

        @Override
        protected String run() throws Exception {
            int value = random.nextInt(200);
            System.out.println("helloWorld2() costs " + value + " ms.");
            Thread.sleep(value);
            return "Hello, World";
        }

        protected String getFallback(){
            return HystrixDemoController.this.errorContent();
        }
    }


    @GetMapping("/hello-world")
    @HystrixCommand(fallbackMethod = "errorContent",
        commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="100")
        })
    public String helloWorld() throws Exception{
        //如果随机时间大于100，则会触发容错
        int value = random.nextInt(200);
        System.out.println("helloWorld() costs " + value + " ms.");
        Thread.sleep(value);
        return "Hello World";
    }



    public String errorContent(){
        return "Fault";
    }
}
