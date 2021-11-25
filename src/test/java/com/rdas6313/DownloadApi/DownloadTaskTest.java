package com.rdas6313.DownloadApi;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

public class DownloadTaskTest implements DownloadResponse{
    @Test
    void testGetHead() {
        D_file dataFile = new D_file("https://unsplash.com/photos/46yjc9dA8MM/download?ixid=MnwxMjA3fDB8MXxhbGx8M3x8fHx8fDJ8fDE2Mzc4MjU0MTY&force=true", FetchType.HEADER_FETCH);
        DownloadTask task = new DownloadTask(this);
        Thread thread = new Thread(()->{
            task.getHead(dataFile);
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int id, int errorCode, String msg) {
        System.err.println("Error for id: "+id);
        System.err.println("Error code: "+errorCode);
        System.err.println("Error msg: "+msg);
        
    }

    @Override
    public void onHeaderInfo(Map<String, String> data) {
        for(Entry entry: data.entrySet()){
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        
    }

    @Override
    public void onProgress(int id, long downloadedSize, long fileSize) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onStop(int id) {
        // TODO Auto-generated method stub
        
    }
    
}
