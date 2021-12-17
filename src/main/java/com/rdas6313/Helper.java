package com.rdas6313;

import java.io.File;

public class Helper {
	
    public static double calculateProgress(long currentSize,long totalSize){
		if(totalSize < currentSize || totalSize == 0)
			return -1;
        double currentProgress = ((double)currentSize/totalSize);
        System.out.println("Progress "+currentProgress);
        return currentProgress;
    }

    public static String formatTextForSizeAndProgress(long currentSize,long totalSize,double currentProgress){
		String text = "";
		//System.out.println("Total size : "+totalSize);
		if(totalSize == -1 && currentSize >= totalSize)
			text = calculateSizeInText(currentSize)+" / Unknown (Unknown %)";
		else
			 text = calculateSizeInText(currentSize)+" / "+calculateSizeInText(totalSize)+" ( "+ (int)(currentProgress*100)+"% )";
      return text;
    }

    public static String calculateSizeInText(long s) {
        long n = s;
		double ans = n;
		int c = 0;
		while(n > 1000 && c < 3){
			ans = ans / 1000;
			n = n / 1000;
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

	public static boolean createDir(String path){
		File file = new File(path);
		if(!file.isDirectory())
			return file.mkdirs();
		return true;
	}
}
