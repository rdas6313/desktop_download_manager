package com.rdas6313.DownloadApi;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueManager {
    private Queue<Integer> queue = new ConcurrentLinkedQueue<Integer>();
    
    public boolean push(int id){
        boolean isEmpty = queue.isEmpty();
        boolean isInserted = queue.offer(id);
        if(isInserted && isEmpty)
            this.notifyAll();
        return isInserted;
    }

    public int pop(){
        if(queue.isEmpty())
            waitThreads();
        int data = queue.poll();
        return data;
    }

    private void waitThreads() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
