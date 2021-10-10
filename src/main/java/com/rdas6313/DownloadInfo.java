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

    public SimpleStringProperty getSizeAndProgressProperty(){
        return sizeAndProgressText;
    }

    public long getCurrentSize(){
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
        double currentProgress = calculateProgress();
        progress.set(currentProgress);
        String text = calculateSizeInText(currentSize)+" / "+calculateSizeInText(size)+" ( "+ (int)(currentProgress*100)+"% )";
        sizeAndProgressText.set(text);
    }

    private String calculateSizeInText(long s) {
        long n = s;
		double ans = n;
		int c = 0;
		while(n > 1024 && c < 3){
			ans = ans / 1024;
			n = n / 1024;
			c++;
		}
		String data;
		switch(c){
			case 0:
				data = String.format("%.2f",ans) + " B";
				break;
			case 1:
				data = String.format("%.2f",ans) + " KB";
				break;
			case 2:
				data = String.format("%.2f",ans) + " MB";
				break;
			default:
				data = String.format("%.2f",ans) + " GB";
		}

		return data;

    }

    public SimpleDoubleProperty getProgressProperty(){
        return progress;
    }

    public double getProgress(){
        return progress.get();
    }

    private double calculateProgress() {
        double currentProgress = ((double)currentSize/size);
        System.out.println("Progress "+currentProgress);
        return currentProgress;
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
