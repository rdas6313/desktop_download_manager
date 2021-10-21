package com.rdas6313.DataBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rdas6313.DownloadInfo;
import com.rdas6313.Observable;

import org.json.simple.JSONObject;

public class PausedListHandler extends Observable implements DbHandler{
    
    private DbConnector dbConnector;
    private final String TABLE_NAME = Config.PAUSED_TABLE_NAME;
    
    public PausedListHandler(DbConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    @Override
    public void delete(int key_id) {
        boolean isDeleted = dbConnector.delete(TABLE_NAME, Config.KEY_ID_COLUMN, String.valueOf(key_id));
        if(isDeleted)
            notifyObservers(Config.DELETION_SUCCESS_NOTIFICATION, null, null);
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
                Long.parseLong((String)object.get(Config.FILE_SIZE_COLUMN)),
                Long.parseLong((String)object.get(Config.DOWNLOADED_SIZE_COLUMN))
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
        datalist.put(Config.DOWNLOADED_SIZE_COLUMN,String.valueOf(data.getCurrentSize()));
       
        boolean isInserted = dbConnector.insert(datalist, TABLE_NAME);
       
        if(!isInserted)
            notifyObservers(Config.INSERTION_ERROR_NOTIFICATION, null, data);
        else 
            notifyObservers(Config.INSERTION_SUCCESS_NOTIFICATION, null, data);
    }
    
}
