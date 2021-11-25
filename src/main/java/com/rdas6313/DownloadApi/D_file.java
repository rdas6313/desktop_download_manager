package com.rdas6313.DownloadApi;

public class D_file {
    private int id;
    private String name;
    private String url;
    private String storage_location;
    private long size;
    private long download_size;
    private volatile boolean isCancelled;
    private FetchType fetchType;

    public D_file(String name, String url, String storage_location,FetchType fetchType) {
        this.name = name;
        this.url = url;
        this.storage_location = storage_location;
        this.fetchType = fetchType;
    }


    public D_file(String url, FetchType fetchType) {
        this.url = url;
        this.fetchType = fetchType;
    }

    public FetchType getFetchType(){
        return fetchType;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFileName() {
        return name;
    }
    public void setFileName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getStorage() {
        return storage_location;
    }
    public void setStorage(String storage_location) {
        this.storage_location = storage_location;
    }
    public long getFileSize() {
        return size;
    }
    public void setFileSize(long size) {
        this.size = size;
    }
    public long getDownloadedSize() {
        return download_size;
    }
    public void setDownloadedSize(long download_size) {
        this.download_size = download_size;
    }
    public boolean isCancelled() {
        return isCancelled;
    }
    public void cancel(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
    
}
