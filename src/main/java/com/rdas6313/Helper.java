package com.rdas6313;

public class Helper {
	
    public static double calculateProgress(long currentSize,long totalSize){
        double currentProgress = ((double)currentSize/totalSize);
        System.out.println("Progress "+currentProgress);
        return currentProgress;
    }

    public static String formatTextForSizeAndProgress(long currentSize,long totalSize,double currentProgress){
      return calculateSizeInText(currentSize)+" / "+calculateSizeInText(totalSize)+" ( "+ (int)(currentProgress*100)+"% )";
    }

    private static String calculateSizeInText(long s) {
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
}
