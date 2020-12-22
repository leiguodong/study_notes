package com.thread.threadPool;

/**
 * Created by lei on 2020/7/19.
 */
public class Worker extends Thread{
    private String name = "任务";
    public Worker(int num){
        this.name = name+num;
    }
    public void run() {
        System.out.println(Thread.currentThread()+"我在执行"+name);
    }
}
