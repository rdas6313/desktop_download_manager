package com.rdas6313.DownloadApi;

public interface DownloadRequest {
    int getInfo(String url);
    int download(String url,String storage,String filename);
    boolean cancel(int id);
    void stopService();
}
