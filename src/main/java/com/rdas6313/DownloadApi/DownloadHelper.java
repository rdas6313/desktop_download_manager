package com.rdas6313.DownloadApi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
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
        int name_size = 60;
        if(parts.length >= 2 && parts[parts.length-1].matches("(.+).(.+)")){
            String small_data = parts[parts.length-1].replaceAll("\"", "");
            if(small_data.length() > name_size)
                return small_data.substring(small_data.length()-name_size);
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
        System.out.println("Response code for "+httpURLConnection.getURL().getHost()+" "+httpURLConnection.getResponseCode());
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
        HttpURLConnection.setFollowRedirects(false);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        URL url_obj = new URL(address);  
        conn = (HttpURLConnection)url_obj.openConnection();
        String protocol = url_obj.getProtocol();
        String host = url_obj.getHost().toString()+":";
        if(protocol == null || protocol.isEmpty())
            throw new ProtocolException("Protocol not mentioned.");
        if(protocol.toLowerCase().equals("https"))
            host += DownloadApiConfig.REMOTE_HTTPS_SERVER_PORT;
        else if(protocol.toLowerCase().equals("http"))
            host += DownloadApiConfig.REMOTE_HTTP_SERVER_PORT;
        else
            throw new ProtocolException("Unsupported protocol");
        conn.setRequestProperty("Host", host);
        conn.setRequestProperty(DownloadApiConfig.USER_AGENT_REQUEST_HEADER, DownloadApiConfig.USER_AGENT_REQUEST_HEADER_VALUE);
        conn.setRequestMethod(req_method);
        logRequestHeaders(conn);
        return conn;
    }

    public static void logRequestHeaders(HttpURLConnection conn){
        System.out.println("......... Request Headers ..............");
        System.out.println("Connecting to : "+conn.getURL());
        System.out.println("Request Method : "+conn.getRequestMethod());
        Map<String, List<String>> data = conn.getRequestProperties();
        for(String key:data.keySet()){
            System.out.println(key+" : "+data.get(key));
        }
        System.out.println("......................... ..............");
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

    public static boolean isHttpAndHttps(String url) throws MalformedURLException {
        URL url_object = new URL(url);
        return (!url_object.getProtocol().equals("http") && !url_object.getProtocol().equals("https"));
    }

    public static Boolean checkResumeSupport(HttpURLConnection conn) {
        String accept_range_header = "Accept-Ranges";
        return !(conn.getHeaderField(accept_range_header) == null || conn.getHeaderField(accept_range_header) == "none");
    }

    public static void deleteLocalFile(String file_name, String path) throws IllegalArgumentException, RuntimeException{
        if(file_name == null || file_name.isEmpty() || path == null || path.isEmpty())
            throw new IllegalArgumentException("name or path is empty or null");
        else if(!file_name.matches("(.+).(.+)"))
            throw new IllegalArgumentException("invalid file name");
        File dir = new File(path);
        if(!dir.isDirectory())
            throw new IllegalArgumentException("path is not a directory");

        File file = new File(path+"/"+file_name);
        try{
            if(file.exists()) {
                file.delete();
            }
        }catch (Exception e){
            throw new RuntimeException("Unable to delete file: "+e.getMessage());
        }
    }
}

