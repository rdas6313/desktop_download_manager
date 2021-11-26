package com.rdas6313.ApiConnection;

import java.util.HashMap;
import java.util.Map;

import com.rdas6313.Observable;
import com.rdas6313.DownloadApi.DownloadApiConfig;
import com.rdas6313.DownloadApi.DownloadRequest;
import com.rdas6313.DownloadApi.DownloadResponse;
import com.rdas6313.DownloadApi.Manager;
import com.rdas6313.Preference.PrefConfig;
import com.rdas6313.Preference.PreferenceHandler;

import org.json.simple.JSONObject;

import javafx.application.Platform;

public class DesktopDownloadConnector extends Observable implements Request,DownloadResponse{

    private DownloadRequest request;
    private PreferenceHandler pHandler;
    
    public DesktopDownloadConnector(PreferenceHandler preferenceHandler){
        pHandler = preferenceHandler;
        int threadCount = 0;
        if(pHandler != null)
            threadCount = pHandler.retrieveInt(PrefConfig.THREAD_LIMIT);
        request = new Manager(threadCount, this);
    }

    @Override
    public void info(String url) {
        request.getInfo(url);
    }

    @Override
    public int startDownload(String url, String filename, String saveLocation) {
        return request.download(url, saveLocation, filename);
        
    }

    @Override
    public void stopDownload(int download_id) {
        request.cancel(download_id);
    }

    @Override
    public void onError(int id, int errorCode, String msg) {
        HashMap<String,Object> data = new HashMap<String,Object>();
        data.put(DataCodes.DOWNLOAD_ID,id);
        Platform.runLater(()->{
            notifyObservers(ResponseCodes.ON_ERROR, null, new JSONObject(data));
        });
        
    }

    @Override
    public void onHeaderInfo(Map<String, String> headerData) {
        Map<String,Object> data = new HashMap<String,Object>();
        data.put(DataCodes.FILE_NAME,headerData.get(DownloadApiConfig.FILE_NAME));
        data.put(DataCodes.FILE_SIZE,Long.parseLong(headerData.get(DownloadApiConfig.FILE_SIZE)));
        data.put(DataCodes.URL,headerData.get(DownloadApiConfig.URL));
        Platform.runLater(() -> {
            notifyObservers(ResponseCodes.ON_INFO, null, new JSONObject(data));
        });
        
    }

    

    @Override
    public void onProgress(int id, String filename, String saveLocation, String url, long downloadedSize,
            long fileSize) {

            HashMap<String,Object> data = new HashMap<String,Object>();
            data.put(DataCodes.FILE_NAME,filename);
            data.put(DataCodes.FILE_SIZE,fileSize);
            data.put(DataCodes.URL,url);
            data.put(DataCodes.SAVED_LOCATION,saveLocation);
            data.put(DataCodes.DOWNLOAD_ID,id);
            data.put(DataCodes.DOWNLOADED_SIZE,downloadedSize);
            Platform.runLater(()->{
                notifyObservers(ResponseCodes.ON_PROGRESS, null, new JSONObject(data));
            });
        
    }

    @Override
    public void onComplete(int id, String filename, String saveLocation, String url, long downloadedSize,
            long fileSize) {
            HashMap<String,Object> data = new HashMap<String,Object>();
            data.put(DataCodes.FILE_NAME,filename);
            data.put(DataCodes.FILE_SIZE,fileSize);
            data.put(DataCodes.URL,url);
            data.put(DataCodes.SAVED_LOCATION,saveLocation);
            data.put(DataCodes.DOWNLOAD_ID,id);
            data.put(DataCodes.DOWNLOADED_SIZE,downloadedSize);
            Platform.runLater(()->{
                notifyObservers(ResponseCodes.ON_COMPLETE, null, new JSONObject(data));
            });
        
    }

    @Override
    public void onStart(int id, String filename, String saveLocation, String url, long downloadedSize, long fileSize) {
        Map<String,Object> data = new HashMap<String,Object>();
        data.put(DataCodes.FILE_NAME,filename);
        data.put(DataCodes.FILE_SIZE,fileSize);
        data.put(DataCodes.DOWNLOADED_SIZE,downloadedSize);
        data.put(DataCodes.URL,url);
        data.put(DataCodes.SAVED_LOCATION,saveLocation);
        data.put(DataCodes.DOWNLOAD_ID,id);
        Platform.runLater(()->{
            notifyObservers(ResponseCodes.ON_START_DOWNLOAD, null, new JSONObject(data));
        });
        
    }

    @Override
    public void onStop(int id, String filename, String saveLocation, String url, long downloadedSize, long fileSize) {
        HashMap<String,Object> data = new HashMap<String,Object>();
         data.put(DataCodes.FILE_NAME,filename);
         data.put(DataCodes.FILE_SIZE,fileSize);
         data.put(DataCodes.URL,url);
         data.put(DataCodes.SAVED_LOCATION,saveLocation);
         data.put(DataCodes.DOWNLOAD_ID,id);
         data.put(DataCodes.DOWNLOADED_SIZE,downloadedSize);
         Platform.runLater(()->{
             notifyObservers(ResponseCodes.ON_STOP_DOWNLOAD, null, new JSONObject(data));
         });
        
    }

    
    
}
