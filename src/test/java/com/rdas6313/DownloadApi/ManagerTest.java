package com.rdas6313.DownloadApi;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

public class ManagerTest implements DownloadResponse{
    @Test
    void testCancel() {

    }

    @Test
    void testDownload() {
        DownloadRequest request = new Manager(3, this);
        int id1 = request.download("https://unsplash.com/photos/46yjc9dA8MM/download?ixid=MnwxMjA3fDB8MXxhbGx8M3x8fHx8fDJ8fDE2Mzc4MjU0MTY&force=true",
            "/home/rdas6313/Music",
            "test1.mp3");
        int id2 = request.download("http://wapfun.in/files/download/id/37100",
            "/home/rdas6313/Music",
            "test2.mp3");
        try {
            int sec = 120;
            Thread.sleep(1000*sec);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
    }

    @Test
    void testGetInfo() {
        DownloadRequest request = new Manager(3, this);
        int id1 = request.getInfo("https://unsplash.com/photos/46yjc9dA8MM/download?ixid=MnwxMjA3fDB8MXxhbGx8M3x8fHx8fDJ8fDE2Mzc4MjU0MTY&force=true");
      //  int id2 = request.getInfo("http://wapfun.in/files/download/id/37100");
        
        try {
            Thread.sleep(10000);
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
    public void onProgress(int id, String filename, String saveLocation, String url, long downloadedSize,
            long fileSize) {
                int percentage = (int)((downloadedSize*100)/fileSize);
                System.out.println("id: "+id+" percentage: "+percentage+" Thread name: "+Thread.currentThread().getName());
           
        
    }

    @Override
    public void onStop(int id, String filename, String saveLocation, String url, long downloadedSize, long fileSize) {
        System.out.println("stopped id: "+id);
        
    }

    @Override
    public void onComplete(int id, String filename, String saveLocation, String url, long downloadedSize,
            long fileSize) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onStart(int id, String filename, String saveLocation, String url, long downloadedSize, long fileSize) {
        // TODO Auto-generated method stub
        
    }

   
    
}
