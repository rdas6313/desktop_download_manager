package com.rdas6313.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;

public class SqlliteConnector implements DbConnector{
    
    private final String SQL_CLASS = "org.sqlite.JDBC";
    private final String DB_PREFIX = "jdbc:sqlite:";
    private final String DATA_BASE_LOCATION = Config.DATABASE_PATH;
    // SqlliteConnector.class.getResource(Config.DATABASE_PATH).toExternalForm();

    public SqlliteConnector() {
        checkDrivers();
    }

    @Override
    public boolean delete(String tableName, String condition, String conditionValue) {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(tableName)
            .append(" WHERE ")
            .append(condition)
            .append(" = ")
            .append(conditionValue)
            .append(";");
        return rawExecute(sql.toString());
    }

    @Override
    public boolean delete(String tableName) {
        StringBuilder sql = new StringBuilder("DELETE FROM ");
        sql.append(tableName)
            .append(";");
        return rawExecute(sql.toString());
    }



    @Override
    public boolean insert(HashMap<String,String>data, String tableName) {
 
        StringBuilder sqlQuery = new StringBuilder("INSERT INTO ");
        sqlQuery.append(tableName)
                .append(" (");
        for(String key : data.keySet()){
            sqlQuery.append(key)
                    .append(",");
        }
        sqlQuery.deleteCharAt(sqlQuery.length()-1);
        sqlQuery.append(")")
                .append(" VALUES ( ");
        for(String value : data.values()){
            sqlQuery.append("\""+value)
                    .append("\",");
        }
        sqlQuery.deleteCharAt(sqlQuery.length()-1);
        sqlQuery.append(" );");
        
        System.out.println(" QUERY String "+sqlQuery.toString());
        
        return rawExecute(sqlQuery.toString());
             
    }

    @Override
    public boolean rawExecute(String sqlQuery) {
        try{
            Connection connection = connect(DATA_BASE_LOCATION);
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            statement.execute();
        }catch(SQLException exception){
            System.out.print(exception.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<JSONObject> readRaw(String sqlQuery) {
        List<JSONObject> dataList = new ArrayList<>();
        try{
            Connection connection = connect(DATA_BASE_LOCATION);
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            ResultSet rs = statement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            
            while(rs.next()){
                
                HashMap<String,Object> data = new HashMap<String,Object>();
                for(int i = 1 ; i <= colCount; i++){
                    data.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                dataList.add(new JSONObject(data));
            }
        }catch(SQLException exception){
            System.out.print(exception.getMessage());
            dataList.clear();
            dataList = null;
        }
        return dataList;
    }

    @Override
    public List<JSONObject> read(String tableName) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(tableName);
        List<JSONObject> dataList = readRaw(sql.toString());
        return dataList;
    }

    public Connection connect(String location){
        Connection connection = null;
        try {
            if(!checkDrivers()){
                connection = null;
                throw new SQLException("Error in SQL Driver");
            }
            System.out.println("Connect DB PATH "+DB_PREFIX+location);
            connection = DriverManager.getConnection(DB_PREFIX + location);
        } catch (SQLException exception) {
            System.out.print(exception.getMessage());
            return null;
        }
        return connection;
    }

    public boolean checkDrivers(){
        try {
            Class.forName(SQL_CLASS);
            DriverManager.registerDriver(new org.sqlite.JDBC());
            return true;
        } catch (ClassNotFoundException | SQLException classNotFoundException) {
            System.out.print(classNotFoundException.getMessage());
            return false;
        }
    }
    
}
