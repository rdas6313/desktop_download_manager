package com.rdas6313.DownloadApi;

import java.util.Map;

public interface DownloadResponse {
    void onProgress(int id,String filename,String saveLocation,String url,long downloadedSize,long fileSize);
    void onStop(int id,String filename,String saveLocation,String url,long downloadedSize,long fileSize);
    void onError(int id,int errorCode,String msg);
    void onHeaderInfo(Map<String,String> data);
    void onComplete(int id,String filename,String saveLocation,String url,long downloadedSize,long fileSize);
    void onStart(int id,String filename,String saveLocation,String url,long downloadedSize,long fileSize);
}
