package com.rdas6313.DownloadApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Manager implements DownloadRequest{

    private RunningListManager runningList;
    private Queue<Integer> queue;
    private List<WorkerThread> threads;
    private boolean shouldStopService;
    private DownloadResponse response;

    public Manager(int no_of_thread,DownloadResponse response) {
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
        System.out.println("DownloadApi Stopping Download for id: "+id);
        return true;
    }

    @Override
    public int download(String url, String storage, String filename) {
        if(shouldStopService)
            return -1;
        D_file data = new D_file(filename, url, storage,FetchType.DATA_FETCH);
        int id = makeRequest(data);
       // response.onStart(id, filename, storage, url, 0, 0);
        return id;
    }

    @Override
    public int getInfo(String url) {
        if(shouldStopService)
            return -1;
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

    @Override
    public void stopService() {
        for(WorkerThread thread : threads){
            thread.stopThread();
        }
        shouldStopService = true;
    }

    
    
}
