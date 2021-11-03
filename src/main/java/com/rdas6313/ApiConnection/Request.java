package com.rdas6313.ApiConnection;

public interface Request {
    void info(String url);
    void startDownload(String url,String filename,String saveLocation);
    void stopDownload(int download_id);
}
