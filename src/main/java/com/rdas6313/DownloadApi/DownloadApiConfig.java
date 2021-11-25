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
}
