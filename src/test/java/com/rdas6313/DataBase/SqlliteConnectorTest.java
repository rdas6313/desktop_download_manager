package com.rdas6313.DataBase;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;

import com.rdas6313.DownloadInfo;

import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class SqlliteConnectorTest {
    private final String table = Config.PAUSED_TABLE_NAME;
    private DbConnector connector = (DbConnector) new SqlliteConnector();
    @Test
    public void insertTest(){
    
        DownloadInfo data = new DownloadInfo("http://", "vigi vagi.mp3", "file://", 1, 42000,2000);
        HashMap<String,String> datalist = new HashMap<>();
        datalist.put(Config.FILE_NAME_COLUMN,data.getFilename());
        datalist.put(Config.URL_COLUMN,data.getUrl());
        datalist.put(Config.SAVED_LOCATION_COLUMN,data.getStorageLocation());
        datalist.put(Config.FILE_SIZE_COLUMN,String.valueOf(data.getSize()));
        datalist.put(Config.DOWNLOADED_SIZE_COLUMN,String.valueOf(data.getCurrentSize()));
        boolean isInserted = connector.insert(datalist, table);
        Assertions.assertTrue(isInserted);

    }

    @Test
    public void driverCheckingTest(){
        SqlliteConnector sqlliteConnector = (SqlliteConnector) connector;
        assertTrue( sqlliteConnector.checkDrivers());
    }

    @Test
    public void connectionTest(){
        SqlliteConnector sqlliteConnector = (SqlliteConnector) connector;
       // String location = SqlliteConnector.class.getResource(Config.DATABASE_PATH).toExternalForm();
       String location = Config.DATABASE_PATH; 
       Assertions.assertNotNull(sqlliteConnector.connect(location));
    }

    @Test
    public void readRawTest(){
        SqlliteConnector sqlliteConnector = (SqlliteConnector) connector;
        String sqlQuery = "select * from "+Config.PAUSED_TABLE_NAME;
        List<JSONObject> list = sqlliteConnector.readRaw(sqlQuery);
        Assertions.assertEquals(5, list.size());
        for(int i = 0;i < list.size();i++){
            JSONObject object = list.get(i);
            System.out.println(object.toString());
        }
    }

    @Test
    public void readTest(){
        SqlliteConnector sqlliteConnector = (SqlliteConnector) connector;
        List<JSONObject> list = sqlliteConnector.read(table);
        Assertions.assertEquals(3, list.size());
    }
}
