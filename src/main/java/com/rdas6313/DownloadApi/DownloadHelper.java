package com.rdas6313.DownloadApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DownloadHelper {

    public static HttpURLConnection setHeaderRange(long startRange,long endRange,HttpURLConnection conn) throws NullPointerException{
      
        if(conn == null)
            throw new NullPointerException("Connection object can't be Null in Header Range");
        else if(startRange == endRange && startRange > 0)
            throw new RuntimeException("start Range and End Range are same.it may possible that whole file has already been downloaded");
        else if(startRange < endRange)
            conn.setRequestProperty("range", "bytes="+startRange+"-"+endRange);
        
        return conn;

    }

    public static long getLocalFileSize(String file_name,String path) throws IllegalArgumentException, FileNotFoundException{
        if(file_name == null || file_name.isEmpty() || path == null || path.isEmpty())
            throw new IllegalArgumentException("name or path is empty or null");
        else if(!file_name.matches("(.+).(.+)"))
            throw new IllegalArgumentException("invalid file name");
        File dir = new File(path);
        if(!dir.isDirectory())
            throw new IllegalArgumentException("path is not a directory");
        
        File file = new File(path+"/"+file_name);
        if(file.exists())
            return file.length();
        return 0;
    }

    public static void isDirExist(String path)throws IllegalArgumentException{
        if(path == null || path.isEmpty())
            throw new IllegalArgumentException("Path can't be empty");
        File dir = new File(path);
        if(!dir.isDirectory())
            throw new IllegalArgumentException("Path is not a Directory");
    }

    public static void putFilenameInHeader(Map<String,String>data,String name){
        if(data == null){
            throw new NullPointerException("Null object.");
        }
        if(name == null)
            data.put(DownloadApiConfig.FILE_NAME,"unknown");
        else    
            data.put(DownloadApiConfig.FILE_NAME,name);
    }

    public static String calculateName(HttpURLConnection conn){
        if(conn == null)
            throw new NullPointerException("Encountered Null Object while calculating names.");
        if(conn.getHeaderField("content-disposition") != null && conn.getHeaderField("content-disposition").matches("attachment(.*)")){
            String content_disposition = conn.getHeaderField("content-disposition");
            String parts[] = content_disposition.split(";");
            if(parts.length >= 2){
                parts = parts[1].split("=");
                if(parts.length == 2)
                    return parts[1].replaceAll("\"", "");
            }
        }

        String uri = conn.getURL().getFile();
        String parts[] = uri.split("/");
        if(parts.length >= 2 && parts[parts.length-1].matches("(.+).(.+)")){
            String small_data = parts[parts.length-1].replaceAll("\"", "");
            if(small_data.length() > 40)
                return small_data.substring(small_data.length()-10);
            return small_data;
        }

        return null;
    }

    public static HttpURLConnection putHeadersInData(HttpURLConnection conn,Map<String,String>data) throws NullPointerException{
        if(conn == null || data == null)
            throw new NullPointerException("Unable to put data beacuause of Null objects.");
       
        boolean should_download_dialog_appear = (conn.getHeaderField("content-disposition") != null) ? conn.getHeaderField("content-disposition").matches("attachment(.*)") : false;
        boolean isResumeable = (conn.getHeaderField("accept-ranges") != null) ? conn.getHeaderField("accept-ranges").contains("bytes") : false; 
        data.put(DownloadApiConfig.FILE_SIZE,conn.getHeaderField("content-length"));
        data.put(DownloadApiConfig.RESUME_SUPPORT,isResumeable+"");
        data.put(DownloadApiConfig.CONTENT_RANGE,conn.getHeaderField("content-range"));
        data.put(DownloadApiConfig.IS_DOWNLOADABLE,should_download_dialog_appear+"");
        data.put(DownloadApiConfig.LAST_MODIFIED,conn.getHeaderField("Last-Modified"));
        data.put(DownloadApiConfig.URL,conn.getURL().toString());
        return conn;
    }

    public static HttpURLConnection checkResponseCode(HttpURLConnection httpURLConnection) throws IOException,RuntimeException,NullPointerException{
        if(httpURLConnection == null)
            throw new NullPointerException(DownloadApiConfig.NULL_OBJECT_IN_CHECKRESPONSECODE);
        
        int code = -1;
        
        code = httpURLConnection.getResponseCode();
       // System.out.println("Response code for "+httpURLConnection.getURL().getHost()+" "+httpURLConnection.getResponseCode());
        if(code >=300 && code < 400){
            throw new RuntimeException(DownloadApiConfig.REDIRECTION_MSG);
        }else if(code >= 400 && code < 500){
            throw new RuntimeException(DownloadApiConfig.CLIENT_SIDE_ERROR_MSG+" "+code);
        }else if(code >= 500){
            throw new RuntimeException(DownloadApiConfig.SERVER_SIDE_ERROR_MSG+" "+code);
        }else if(code != 200 && code != 206){
            throw new RuntimeException(DownloadApiConfig.UNKNOWN_ERROR_MSG+" "+code);
        }

        return httpURLConnection;
    }

    public static HttpURLConnection connect(String address,String req_method) throws MalformedURLException,IOException,ProtocolException{
        HttpURLConnection conn = null;
        address = encodeSpace(address);
        URL url_obj = new URL(address);  
        conn = (HttpURLConnection)url_obj.openConnection();
        conn.setRequestProperty(url_obj.getHost().toString(), DownloadApiConfig.REMOTE_SERVER_PORT);
        conn.setRequestProperty(DownloadApiConfig.USER_AGENT_REQUEST_HEADER, DownloadApiConfig.USER_AGENT_REQUEST_HEADER_VALUE);
        System.out.println("Host:Port = "+url_obj.getHost().toString()+":80");
        conn.setRequestMethod(req_method);
        return conn;
    }

	public static long calculatePercentage(long bytesRead, long remote_filesize) throws RuntimeException {
		if(bytesRead == -1 || remote_filesize == -1 || bytesRead > remote_filesize)
            throw new RuntimeException(DownloadApiConfig.PERCENTAGE_CALCULATION_ERROR);
        long percentage = (bytesRead*100)/remote_filesize;
        return percentage;
    }

    public static long getRemoteFileSize(HttpURLConnection conn) throws NullPointerException{
        if(conn == null)
            throw new NullPointerException("getting Null connection object while calculating remote file size");
        String value = conn.getHeaderField("content-length");
        if(value == null)
            return 0;
        return Long.parseLong(value);
    }

    public static String encodeSpace(String data){
        return data.replaceAll(" ", "%20");
    }
}

