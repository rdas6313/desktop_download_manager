package com.rdas6313.DownloadApi;

import java.util.Map;

public interface DownloadResponse {
    void onProgress(int id,long downloadedSize,long fileSize);
    void onStop(int id);
    void onError(int id,int errorCode,String msg);
    void onHeaderInfo(Map<String,String> data);
}
