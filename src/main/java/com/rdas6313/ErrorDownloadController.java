package com.rdas6313;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

import com.rdas6313.DataBase.DbConfig;
import com.rdas6313.DataBase.DbHandler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class ErrorDownloadController extends TitleController implements Initializable,BtnEventNotifiable,PropertyChangeListener {
    
    @FXML
    private AnchorPane rootView;

    @FXML
    private ListView<DownloadInfo> listView;

    private DbHandler dbHandler;
    
    public ObservableList<DownloadInfo> downloadInfoObservableList;

    public ErrorDownloadController(DbHandler dbHandler) {
        this.dbHandler = dbHandler;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        downloadInfoObservableList = FXCollections.observableArrayList();
        listView.setItems(downloadInfoObservableList);
        listView.setOrientation(Orientation.VERTICAL);
        listView.setCellFactory(new Callback<ListView<DownloadInfo>,ListCell<DownloadInfo>>(){

            @Override
            public ListCell<DownloadInfo> call(ListView<DownloadInfo> param) {
                
                return new ErrorDownloadListCell(ErrorDownloadController.this);
            }
            
        });

        loadList();
    }
    
    @Override
    public void onBtnEventOccured(int index, BtnEventType eventType) {
        switch(eventType){
            case DELETE_EVENT:
                onDelete(index);
                break;
            default:
                break;
        }
        
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()){
            case DbConfig.INSERTION_SUCCESS_NOTIFICATION:
                onInsert((DownloadInfo)evt.getNewValue());
                break;
        }
        
    }

    private void onInsert(DownloadInfo data) {
        try {
            if(downloadInfoObservableList == null)
                return;
            downloadInfoObservableList.add(data);
        } catch (Exception e) {
            System.err.println(getClass().getName()+" onInsert :"+e.getMessage());
        }
    }

    private void loadList() {
        try {
            downloadInfoObservableList.addAll(dbHandler.getList());
        } catch (Exception e) {
            System.err.println(getClass().getName()+" loadCompletedList :"+e.getMessage());
        }  
    }

    private void onDelete(int index) {
        try {
            DownloadInfo data = (DownloadInfo) downloadInfoObservableList.remove(index);
            dbHandler.delete(data.getId());
        } catch (Exception e) {
            System.err.println(getClass().getName()+" onDelete :"+e.getMessage());
        }
    }

    @Override
    public String getWindowTitle() {
        return com.rdas6313.Config.ERROR_DOWNLOAD_TITLE;
    }

    @Override
    protected String getFxmlPath() {
        return com.rdas6313.Config.LOAD_ERROR_DOWNLOAD;
    }
}
