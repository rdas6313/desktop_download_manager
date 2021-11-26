package com.rdas6313.ApiConnection;

public interface Request {
    void info(String url);
    int startDownload(String url,String filename,String saveLocation);
    void stopDownload(int download_id);
    void stopDownloadService();
}
