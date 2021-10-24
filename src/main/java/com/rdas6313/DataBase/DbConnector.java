package com.rdas6313.DataBase;

import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

public interface DbConnector {
   
    List<JSONObject> read(String tableName);
    List<JSONObject> readRaw(String sqlQuery);
    int insert(HashMap<String,String>data,String tableName);
    int delete(String tableName,String condition,String conditionValue);
    boolean delete(String tableName);
    boolean rawExecute(String sqlQuery);

}
