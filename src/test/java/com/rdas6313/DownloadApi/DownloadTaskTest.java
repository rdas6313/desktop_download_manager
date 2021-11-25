package com.rdas6313.DownloadApi;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

public class DownloadTaskTest implements DownloadResponse{

    @Test
    void testGetHead() {
        D_file dataFile = new D_file(
        "http://wapfun.in/files/download/id/37100", FetchType.HEADER_FETCH);
        DownloadTask task = new DownloadTask(this);
        Thread thread = new Thread(()->{
            task.headerDownload(dataFile);
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
        int percentage = (int)((downloadedSize*100)/fileSize);
        System.out.println("id: "+id+" percentage: "+percentage+" Thread name: "+Thread.currentThread().getName());
    }

    @Override
    public void onStop(int id) {
        System.out.println("stopped id: "+id);
        
    }

    @Test
    void testFileDownload() {
        D_file dataFile = new D_file("testfile", 
        "http://wapfun.in/files/download/id/37100",
            "/home/rdas6313/Music",
            FetchType.DATA_FETCH
        );
        DownloadTask task = new DownloadTask(this);
        Thread thread = new Thread(()->{
            task.fileDownload(dataFile);
        });
        thread.start();
        try {
            /* Thread.sleep(3000);
            dataFile.cancel(true); */
            System.out.println("Joining soon");
            thread.join();
           
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
