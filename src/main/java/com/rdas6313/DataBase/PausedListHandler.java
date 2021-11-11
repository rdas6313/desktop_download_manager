package com.rdas6313.DataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rdas6313.DownloadInfo;
import com.rdas6313.Observable;

import org.json.simple.JSONObject;

public class PausedListHandler extends Observable implements DbHandler{
    
    private DbConnector dbConnector;
    private final String TABLE_NAME = DbConfig.PAUSED_TABLE_NAME;
    
    public PausedListHandler(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
        InitTable();
    }

    private void InitTable() {
        StringBuilder sqlBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sqlBuilder.append(TABLE_NAME)
                .append(" (")
                .append(DbConfig.KEY_ID_COLUMN + " INTEGER PRIMARY KEY, ")
                .append(DbConfig.FILE_NAME_COLUMN + " TEXT NOT NULL, ")
                .append(DbConfig.URL_COLUMN + " TEXT NOT NULL, ")
                .append(DbConfig.SAVED_LOCATION_COLUMN + " TEXT NOT NULL, ")
                .append(DbConfig.FILE_SIZE_COLUMN + " TEXT NOT NULL, ")
                .append(DbConfig.DOWNLOADED_SIZE_COLUMN + " TEXT NOT NULL")
                .append(");");
        
        if(!dbConnector.rawExecute(sqlBuilder.toString()))
            System.err.println(getClass().getSimpleName()+" InitTable: unable to create table");
    }

    @Override
    public void delete(int key_id) {
        int key = dbConnector.delete(TABLE_NAME, DbConfig.KEY_ID_COLUMN, String.valueOf(key_id));
        if(key != -1){
            notifyObservers(DbConfig.DELETION_SUCCESS_NOTIFICATION, null, null);
            System.out.println(getClass().getName()+" delete: data with key"+ key_id+ "deleted");
        }
        else
            notifyObservers(DbConfig.DELETION_ERROR_NOTIFICATION, null, null);
    }

    @Override
    public List<DownloadInfo> getList() {
        List<JSONObject> objects = dbConnector.read(TABLE_NAME);
        List<DownloadInfo> datalist = new ArrayList<>();
        for(JSONObject object : objects){
            DownloadInfo downloadInfo = new DownloadInfo(
                (String) object.get(DbConfig.URL_COLUMN),
                (String) object.get(DbConfig.FILE_NAME_COLUMN),
                (String) object.get(DbConfig.SAVED_LOCATION_COLUMN),
                (int)    object.get(DbConfig.KEY_ID_COLUMN),
                Long.parseLong((String)object.get(DbConfig.FILE_SIZE_COLUMN)),
                Long.parseLong((String)object.get(DbConfig.DOWNLOADED_SIZE_COLUMN))
            );
            datalist.add(downloadInfo);
        }
        return datalist;
    }

    @Override
    public void insert(DownloadInfo data) {
       
        HashMap<String,String>datalist = new HashMap<String,String>();
        datalist.put(DbConfig.FILE_NAME_COLUMN,data.getFilename());
        datalist.put(DbConfig.URL_COLUMN,data.getUrl());
        datalist.put(DbConfig.SAVED_LOCATION_COLUMN,data.getStorageLocation());
        datalist.put(DbConfig.FILE_SIZE_COLUMN,String.valueOf(data.getSize()));
        datalist.put(DbConfig.DOWNLOADED_SIZE_COLUMN,String.valueOf(data.getCurrentSize()));
       
        int key = dbConnector.insert(datalist, TABLE_NAME);
       
        if(key == -1)
            notifyObservers(DbConfig.INSERTION_ERROR_NOTIFICATION, null, data);
        else{
            data = insertKeyToData(data,key);             
            notifyObservers(DbConfig.INSERTION_SUCCESS_NOTIFICATION, null, data);
            System.out.println(getClass().getName()+" Insert: "+data.getFilename()+" Inserted");
        }
    }

    private DownloadInfo insertKeyToData(DownloadInfo data,int key) {
       DownloadInfo info = new DownloadInfo(
           data.getUrl(),
           data.getFilename(),
           data.getStorageLocation(),
           key,
           data.getSize(),
           data.getCurrentSize()
       );
       return info;
    }
    
}
