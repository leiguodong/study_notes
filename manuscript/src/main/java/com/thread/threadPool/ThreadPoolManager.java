package com.thread.threadPool;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by lei on 2020/7/19.
 */
public class ThreadPoolManager {
    private List<Runnable> coreList = new Vector<Runnable>();
    private List<Runnable> freeList = new Vector<Runnable>();
    private BlockingDeque<Runnable> blockQueue ;
    private int corePoolNum = 3;
    private int freeThreadNum = 2;
    private int maxThreadNum = 5;
    private ThreadPoolManager(int corePoolNum,int freeThreadNum,int maxThreadNum,int capacity){
        this.corePoolNum = corePoolNum;
        this.freeThreadNum = freeThreadNum;
        this.maxThreadNum = maxThreadNum;
        this.blockQueue = new LinkedBlockingDeque(capacity);
    }
    public static ThreadPoolManager getInstance(){
        return  new ThreadPoolManager(3,2,5,5);
    }
    public void excute(Worker worker){
        if(coreList.size()<=corePoolNum){
            coreList.add(worker);
            worker.start();
        }else{

        }
        blockQueue.offer(worker);
    }
}
