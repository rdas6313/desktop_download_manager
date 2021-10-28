package com.rdas6313.DataBase;

public class Config {
    public final static String DATABASE_PATH = "abc.db";
    //Columns
    public final static String KEY_ID_COLUMN = "key_id";
    public final static String FILE_NAME_COLUMN = "file_name";
    public final static String URL_COLUMN = "url";
    public final static String FILE_SIZE_COLUMN = "file_size";
    public final static String DOWNLOADED_SIZE_COLUMN = "downloaded_size";
    public final static String SAVED_LOCATION_COLUMN = "saved_location";
    //download table
    public static final String PAUSED_TABLE_NAME = "paused_table";
    public static final String COMPLETED_TABLE_NAME = "completed_table";
    //Notification Messages
    public static final String INSERTION_ERROR_NOTIFICATION = "DATABASE_INSERTION_ERROR";
    public static final String INSERTION_SUCCESS_NOTIFICATION = "DATABASE_INSERTION_ALERT";
    public static final String DELETION_SUCCESS_NOTIFICATION = "DATABASE_DELETION_ALERT";
    public static final String DELETION_ERROR_NOTIFICATION = "DATABASE_DELETION_ERROR";
    
   

}
