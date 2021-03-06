package com.rdas6313.DownloadApi;

import java.util.NoSuchElementException;
import java.util.Queue;

public class WorkerThread extends Thread{
    private RunningListManager runningList;
    private Queue<Integer> queue;
    private DownloadResponse response;
    private DownloadTask task;
    private boolean shouldStopThread;

    public WorkerThread(RunningListManager runningList, Queue<Integer> queue, DownloadResponse response) {
        this.runningList = runningList;
        this.queue = queue;
        this.response = response;
        task = new DownloadTask(response);
        shouldStopThread = false;
    }

    @Override
    public void run() {
    
      
        while(!shouldStopThread){
            int id = getIdFromQueue();
            System.out.println(Thread.currentThread().getName()+" working on id: "+id);
            D_file data = runningList.get(id);
            if(data == null){
                System.err.println("Running Data not found with key "+id);
                continue;
            }
            switch (data.getFetchType()) {
                case HEADER_FETCH:
                    task.headerDownload(data);
                    break;
                case DATA_FETCH:
                    task.fileDownload(data);
                    break;
                default:
                    break;
            }
            
            runningList.remove(id);
        }
        
        
    
    }

    private int getIdFromQueue() {
        boolean isWaiting = true;
        int id = -1;
        while(isWaiting){
            try{
                isWaiting = false;
                id = queue.remove();
            }catch(NoSuchElementException e){
                isWaiting = true;
                waitThread();
            }
        }
        return id;
    }

    private void waitThread() {
        synchronized(queue){
            try {
                System.out.println("Waiting "+Thread.currentThread().getName());
                queue.wait();
                System.out.println("Waking up "+Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread(){
        shouldStopThread = true;
    }
       
}
