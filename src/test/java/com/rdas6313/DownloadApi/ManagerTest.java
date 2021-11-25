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

    }

    @Test
    void testGetInfo() {
        DownloadRequest request = new Manager(3, this);
        int id1 = request.getInfo("https://unsplash.com/photos/46yjc9dA8MM/download?ixid=MnwxMjA3fDB8MXxhbGx8M3x8fHx8fDJ8fDE2Mzc4MjU0MTY&force=true");
        
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       // int id3 = request.getInfo("http://"); 
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
