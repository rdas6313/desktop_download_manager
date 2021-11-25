package com.rdas6313.DownloadApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Manager implements DownloadRequest{

    private RunningListManager runningList;
    private Queue<Integer> queue;
    private List<WorkerThread> threads;
    private int totalThreads;
    private DownloadResponse response;

    public Manager(int no_of_thread,DownloadResponse response) {
        totalThreads = no_of_thread;
        this.response = response;
        runningList = new RunningListManager();
        queue = new ConcurrentLinkedQueue<Integer>();
        threads = new ArrayList<>();
        for(int i=0;i < no_of_thread;i++){
            WorkerThread thread = new WorkerThread(runningList, queue, response);
            thread.start();
            threads.add(thread);
        }
    }

    @Override
    public boolean cancel(int id) {
        D_file data = runningList.get(id);
        if(data == null)
            return false;
        data.cancel(true);
        if(response != null)
            response.onStop(id);
        return true;
    }

    @Override
    public int download(String url, String storage, String filename) {
        D_file data = new D_file(filename, url, storage,FetchType.DATA_FETCH);
        return makeRequest(data);
    }

    @Override
    public int getInfo(String url) {
        D_file data = new D_file(url,FetchType.HEADER_FETCH);
        return makeRequest(data);
    }

    private int makeRequest(D_file data){
        int id = runningList.store(data);
        data.setId(id);
        boolean isSuccess = queue.offer(id);
        if(!isSuccess)
            return -1;
        synchronized(queue){
            queue.notifyAll();
        }
        return id;
    }
    
}
