package com.rdas6313.DownloadApi;

public class DownloadApiConfig {
    public final static String HEAD_REQUEST = "HEAD";
    public final static String REDIRECT_HEADER = "location";
    public final static String REDIRECTION_REGEX = "Redirection";
    public static final String PERCENTAGE_CALCULATION_ERROR = "unable to calculate percentage.";
    public static final String REDIRECTION_MSG = "Redirection";
    public static final String CLIENT_SIDE_ERROR_MSG = "Client Side Problem ";
    public static final String SERVER_SIDE_ERROR_MSG = "Server Side Problem";
    public static final String UNKNOWN_ERROR_MSG = "Unknown problem";
    public static final String NULL_OBJECT_IN_CHECKRESPONSECODE = "Null object checkResponseCode";
    //Exception Code for DownloadTask
    public static final int NULL_EXCEPTION_CODE = 1000;
    public static final int URL_EXCEPTION_CODE = 2000;
    public static final int INPUT_OUTPUT_EXCEPTION_CODE = 3000;
    public static final int RUNTIME_EXCEPTION_CODE = 4000;
    public static final int ARUGUMENT_EXCEPTION_CODE = 5000;
    public static final int FILE_NOT_FOUND_EXCEPTION_CODE = 6000;
    public static final int PROTOCOL_EXCEPTION_CODE = 7000;
    //
    public static final String FILE_SIZE = "file-size";
    public static final String RESUME_SUPPORT = "resume-support";
    public static final String CONTENT_RANGE = "content-range";
    public static final String IS_DOWNLOADABLE = "downloadable-file";
    public static final String LAST_MODIFIED = "last-modified";
    public static final String URL = "url";
    public static final String FILE_NAME = "file-name";
   
    public static final String USER_AGENT_REQUEST_HEADER = "User-Agent";
    public static final String USER_AGENT_REQUEST_HEADER_VALUE = "Download_Manager/1.0";
    public static final String REMOTE_HTTP_SERVER_PORT = "80";
    public static final String REMOTE_HTTPS_SERVER_PORT = "443";
}
