package com.rdas6313;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class DownloadInfo {
    private String url;
    private String filename;
    private String storageLocation;
    private int id;
    private long size;
    private long currentSize;
    private SimpleStringProperty sizeAndProgressText;
    private SimpleDoubleProperty progress;
    
    public DownloadInfo(String url, String filename, String storageLocation, int id,long size) {
        this.url = url;
        this.filename = filename;
        this.storageLocation = storageLocation;
        this.id = id;
        this.size = size;
        this.currentSize = 0;
        this.sizeAndProgressText = new SimpleStringProperty();
        this.progress = new SimpleDoubleProperty(0);
    }

    public DownloadInfo(String url, String filename, String storageLocation, int id,long size,long currentSize) {
        this.url = url;
        this.filename = filename;
        this.storageLocation = storageLocation;
        this.id = id;
        this.size = size;
        this.currentSize = currentSize;
        this.sizeAndProgressText = new SimpleStringProperty(
            Helper.formatTextForSizeAndProgress(currentSize,size,
                    Helper.calculateProgress(currentSize, size)
            )
        );
        this.progress = new SimpleDoubleProperty(
            Helper.calculateProgress(currentSize, size)
        );
    }

    public SimpleStringProperty getSizeAndProgressProperty(){
        return sizeAndProgressText;
    }

    public long getCurrentSize(){
        return currentSize;
    }

    public void setFileSize(long size){
        this.size = size;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
        double currentProgress = Helper.calculateProgress(currentSize, size);
        progress.set(currentProgress);
        String text = Helper.formatTextForSizeAndProgress(currentSize, size, currentProgress);
        sizeAndProgressText.set(text);
    }

  

    public SimpleDoubleProperty getProgressProperty(){
        return progress;
    }

    public double getProgress(){
        return progress.get();
    }

    

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public String getFilename() {
        return filename;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public int getId() {
        return id;
    }

         
}
