package com.moses.cloud.sample.hystrixTheoryStudy;

import rx.Observer;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.Random;

public class RxJavaDemo {

    public static void main(String[] args) {
        Random r = new Random();
        Single.just("Hello, World")     //just 发布数据
                .subscribeOn(Schedulers.io())   //订阅的线程池 immediate = Thread.currentThread()
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {     //正常结束流程
                        System.out.println("执行结束！");
                    }

                    @Override
                    public void onError(Throwable throwable) {      //异常流程（结束）
                        System.out.println("熔断保护！");
                    }

                    @Override
                    public void onNext(String s) {      //数据消费 s = "Hello World"
                        //如果随机时间大于100，那么触发容错
                        int value = r.nextInt(200);
                        System.out.println("helloWorld() costs " + value + " ms.");
                        if(value>100) {
                            throw new RuntimeException("Timeout!");
                        }
                    }
                });
    }
}
