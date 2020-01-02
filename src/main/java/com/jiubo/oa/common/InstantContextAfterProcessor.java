package com.jiubo.oa.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * @desc:
 * @date: 2019-08-15 14:58
 * @author: dx
 * @version: 1.0
 */
@Component
public class InstantContextAfterProcessor implements ApplicationContextAware {

    public static ApplicationContext applicationContext = null;

    private static volatile Boolean isFinishedInit = Boolean.FALSE;

    private static LinkedBlockingQueue waiters = new LinkedBlockingQueue();

    public static LinkedBlockingQueue getWaiters() {
        return waiters;
    }

    public static Boolean getIsFinishedInit() {
        return isFinishedInit;
    }

    public static void setIsFinishedInit(Boolean isFinishedInit) {
        InstantContextAfterProcessor.isFinishedInit = isFinishedInit;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> T getService(String name,Class<T> claz){
        if(applicationContext == null) return null;
        return (T) applicationContext.getBean(name);
    }

    //等待程序
    public static void waitUntilFinishInit(){
        if(!InstantContextAfterProcessor.getIsFinishedInit()){
            waiters.add(Thread.currentThread());
            LockSupport.park(Thread.currentThread());
        }
    }
}
