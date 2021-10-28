package com.rdas6313.DataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rdas6313.DownloadInfo;
import com.rdas6313.Observable;

import org.json.simple.JSONObject;

public class CompletedListHandler extends Observable implements DbHandler{

    private DbConnector dbConnector;
    private final String TABLE_NAME = Config.COMPLETED_TABLE_NAME;

    public CompletedListHandler(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public void delete(int key_id) {
        int key = dbConnector.delete(TABLE_NAME, Config.KEY_ID_COLUMN, String.valueOf(key_id));
        if(key != -1){
            notifyObservers(Config.DELETION_SUCCESS_NOTIFICATION, null, null);
            System.out.println(getClass().getName()+" delete: data with key"+ key_id+ "deleted");
        }
        else
            notifyObservers(Config.DELETION_ERROR_NOTIFICATION, null, null);
        
    }

    @Override
    public List<DownloadInfo> getList() {
        List<JSONObject> objects = dbConnector.read(TABLE_NAME);
        List<DownloadInfo> datalist = new ArrayList<>();
        for(JSONObject object : objects){
            DownloadInfo downloadInfo = new DownloadInfo(
                (String) object.get(Config.URL_COLUMN),
                (String) object.get(Config.FILE_NAME_COLUMN),
                (String) object.get(Config.SAVED_LOCATION_COLUMN),
                (int)    object.get(Config.KEY_ID_COLUMN),
                Long.parseLong((String)object.get(Config.FILE_SIZE_COLUMN))
            );
            datalist.add(downloadInfo);
        }
        return datalist;
    }

    @Override
    public void insert(DownloadInfo data) {
        
        HashMap<String,String>datalist = new HashMap<String,String>();
        datalist.put(Config.FILE_NAME_COLUMN,data.getFilename());
        datalist.put(Config.URL_COLUMN,data.getUrl());
        datalist.put(Config.SAVED_LOCATION_COLUMN,data.getStorageLocation());
        datalist.put(Config.FILE_SIZE_COLUMN,String.valueOf(data.getSize()));
       
       
        int key = dbConnector.insert(datalist, TABLE_NAME);
       
        if(key == -1)
            notifyObservers(Config.INSERTION_ERROR_NOTIFICATION, null, data);
        else{
            data = insertKeyToData(data,key);             
            notifyObservers(Config.INSERTION_SUCCESS_NOTIFICATION, null, data);
            System.out.println(getClass().getName()+" Insert: "+data.getFilename()+" Inserted");
        }
        
    }

    private DownloadInfo insertKeyToData(DownloadInfo data,int key) {
        DownloadInfo info = new DownloadInfo(
            data.getUrl(),
            data.getFilename(),
            data.getStorageLocation(),
            key,
            data.getSize()
        );
        return info;
     }
    
}
