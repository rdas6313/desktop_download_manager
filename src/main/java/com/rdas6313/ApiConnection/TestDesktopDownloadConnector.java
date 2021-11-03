package com.rdas6313.ApiConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.rdas6313.Observable;

import org.json.simple.JSONObject;

import javafx.application.Platform;

public class TestDesktopDownloadConnector extends Observable implements Request{

    protected static final int INCREASE_SIZE = 100;
    protected static final int SIZE = 3*1024;
    private List<TestData> list = new ArrayList<TestData>();
    private List<TestData> cancelledList = new ArrayList<TestData>();
    private Thread thread;
    private int count = 0;

    private Thread createThread(){
        return  new Thread(new Runnable(){

            @Override
            public void run() {
                while(list.size() > 0){
                    onThread();
                    sleep(2000);
                }
                thread = null;
            }
    
            private void sleep(int i) {
                try {
                    Thread.sleep(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    
        });
    }

    @Override
    public void info(String url) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){

            @Override
            public void run() {
                onInfo("Tum hi ho By Arijit Singh", SIZE,url);
            }
            
        }, 2000);
    }

    protected void onThread() {
        for(int i=0;i<list.size();i++){
            TestData data = list.get(i);
            if(data.downloadSize >= data.filesize){
                list.remove(i);
                onComplete(data.url, data.filename, data.location, data.filesize, data.id);
                return;
            }else if(data.isCanceled){
                list.remove(i);
                return;
            }
            data.downloadSize += INCREASE_SIZE;
            onProgress(data.id, data.filename, data.url, data.location, data.downloadSize, data.filesize);
        }

    }

    private void onInfo(String fileName,long fileSize,String url){
        HashMap<String,Object> data = new HashMap<String,Object>();
        data.put(DataCodes.FILE_NAME,fileName);
        data.put(DataCodes.FILE_SIZE,fileSize);
        data.put(DataCodes.URL,url);
        Platform.runLater(() -> {
            notifyObservers(ResponseCodes.ON_INFO, null, new JSONObject(data));
        });
    }

    @Override
    public void startDownload(String url, String filename, String saveLocation) {

        TestData data = getPausedTestData(url, filename, saveLocation);
        if(data == null)
            data = new TestData(filename,-1,url,saveLocation,SIZE,0);

        data.id = count++;
        data.isCanceled = false;
        list.add(data);
        onStartDownload(url, filename, saveLocation, data.id, SIZE, data.downloadSize);
        if(thread == null){
            thread = createThread();
            thread.start();
        }
        System.out.println(getClass().getSimpleName()+" statDownload: Alive:"+thread.isAlive()+" id: "+thread.getId());
    }

    

    private TestData getPausedTestData(String url, String filename, String saveLocation) {
        for(TestData data: cancelledList){
            if(data.url.equals(url) && data.filename.equals(filename) && data.location.equals(saveLocation))
            {
                cancelledList.remove(data);
                return data;
            }
        }
        return null;
    }

    private void onStartDownload(String url,String fileName,String saveLocation,int id,long fileSize,long downloadedSize){
        HashMap<String,Object> data = new HashMap<String,Object>();
        data.put(DataCodes.FILE_NAME,fileName);
        data.put(DataCodes.FILE_SIZE,fileSize);
        data.put(DataCodes.DOWNLOADED_SIZE,downloadedSize);
        data.put(DataCodes.URL,url);
        data.put(DataCodes.SAVED_LOCATION,saveLocation);
        data.put(DataCodes.DOWNLOAD_ID,id);
        Platform.runLater(()->{
            notifyObservers(ResponseCodes.ON_START_DOWNLOAD, null, new JSONObject(data));
        });
        System.out.println(getClass().getSimpleName()+" onStartDownload");
    }

    @Override
    public void stopDownload(int download_id) {
    //    mainData.isCanceled = true;
        TestData data = getDataUsingDownloadId(download_id);
        data.isCanceled = true;
        cancelledList.add(data);
        onStopDownload(data.id, data.filename, data.url, data.location, data.downloadSize, data.filesize);
        
        System.out.println(getClass().getSimpleName()+" stopDownload");
    }

    private TestData getDataUsingDownloadId(int id){
        for(TestData data: list){
            if(data.id == id)
                return data;
        }
        return null;
    }

    private void onStopDownload(int id, String fileName, String url, String saveLocation, long currentSize, long size){


        HashMap<String,Object> data = new HashMap<String,Object>();
        data.put(DataCodes.FILE_NAME,fileName);
        data.put(DataCodes.FILE_SIZE,size);
        data.put(DataCodes.URL,url);
        data.put(DataCodes.SAVED_LOCATION,saveLocation);
        data.put(DataCodes.DOWNLOAD_ID,id);
        data.put(DataCodes.DOWNLOADED_SIZE,currentSize);
        Platform.runLater(()->{
            notifyObservers(ResponseCodes.ON_STOP_DOWNLOAD, null, new JSONObject(data));
        });
        System.out.println(getClass().getSimpleName()+" onStopDownload");

    }

    private void onProgress(int id, String fileName, String url, String saveLocation, long currentSize, long size){
        HashMap<String,Object> data = new HashMap<String,Object>();
        data.put(DataCodes.FILE_NAME,fileName);
        data.put(DataCodes.FILE_SIZE,size);
        data.put(DataCodes.URL,url);
        data.put(DataCodes.SAVED_LOCATION,saveLocation);
        data.put(DataCodes.DOWNLOAD_ID,id);
        data.put(DataCodes.DOWNLOADED_SIZE,currentSize);
        Platform.runLater(()->{
            notifyObservers(ResponseCodes.ON_PROGRESS, null, new JSONObject(data));
        });
        System.out.println(getClass().getSimpleName()+" onProgress");
    }

    private void onComplete(String url, String fileName, String saveLocation, long size, int id){
        HashMap<String,Object> data = new HashMap<String,Object>();
        data.put(DataCodes.FILE_NAME,fileName);
        data.put(DataCodes.FILE_SIZE,size);
        data.put(DataCodes.URL,url);
        data.put(DataCodes.SAVED_LOCATION,saveLocation);
        data.put(DataCodes.DOWNLOAD_ID,id);
        data.put(DataCodes.DOWNLOADED_SIZE,size);
        Platform.runLater(()->{
            notifyObservers(ResponseCodes.ON_COMPLETE, null, new JSONObject(data));
        });
        System.out.println(getClass().getSimpleName()+" onComplete");
    }

    private void onError(){

    }

    class TestData{
        private String filename;
        private int id;
        private String url;
        private String location;
        private long filesize;
        private long downloadSize;
        private boolean isCanceled;
        
        public TestData(String filename, int id, String url, String location, long filesize, long downloadSize) {
            this.filename = filename;
            this.id = id;
            this.url = url;
            this.location = location;
            this.filesize = filesize;
            this.downloadSize = downloadSize;
            isCanceled = false;
        }

        
    }
    
}
