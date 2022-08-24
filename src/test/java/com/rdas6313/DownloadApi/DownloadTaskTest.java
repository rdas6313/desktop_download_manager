package com.rdas6313.DownloadApi;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

public class DownloadTaskTest implements DownloadResponse{

    @Test
    void testGetHead() {
        String url1 = "https://pagalworld.com.se/siteuploads/files/sfd14/6934/Har Har Shambhu Ringtone_320(PagalWorld.com.se).mp3";
        String url0 = "https://pagalworld.com.se/files/download/type/192/id/6045";
        String url = "https://pagalworld.com.se/files/download/type/320/id/6934";
        D_file dataFile = new D_file(
            url, FetchType.HEADER_FETCH);
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
    public void onStop(int id, String filename, String saveLocation, String url, long downloadedSize, long fileSize) {
        System.out.println("stopped id: "+id);
        
    }

    @Test
    void testFileDownload() {
        String url1 = "https://pagalworld.com.se/files/download/type/320/id/6934";
        String url = "https://pagalworld.com.se/files/download/type/192/id/6045";
        String url11 = "http://www.abc.nkang";
        D_file dataFile = new D_file("testfile2.mp3",
            url,
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

    @Override
    public void onProgress(int id, String filename, String saveLocation, String url, long downloadedSize,
            long fileSize) {
                if(downloadedSize <= fileSize && fileSize > 0){
                    int percentage = (int)((downloadedSize*100)/fileSize);
                    System.out.println("filesize: "+fileSize+", downloaded size: "+downloadedSize);
                    System.out.println("id: "+id+" percentage: "+percentage+" Thread name: "+Thread.currentThread().getName());
                }else{
                    System.out.println("id: "+id+" Downloaded Size: "+downloadedSize+" Thread name: "+Thread.currentThread().getName());
                }
        
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
