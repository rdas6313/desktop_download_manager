package com.rdas6313;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.rdas6313.DataBase.Config;
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
import java.awt.Desktop;

public class CompletedDownloadController extends TitleController implements Initializable,BtnEventNotifiable,PropertyChangeListener{

    @FXML
    private AnchorPane rootView;

    @FXML
    private ListView<DownloadInfo> listView;

    private DbHandler dbHandler;

    public ObservableList<DownloadInfo> downloadInfoObservableList = FXCollections.observableArrayList();;
    
    public CompletedDownloadController(DbHandler dbHandler) {
        this.dbHandler = dbHandler;
    }
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setItems(downloadInfoObservableList);
        listView.setOrientation(Orientation.VERTICAL);
        listView.setCellFactory(new Callback<ListView<DownloadInfo>,ListCell<DownloadInfo>>(){

            @Override
            public ListCell<DownloadInfo> call(ListView<DownloadInfo> param) {
                
                return new CompletedDownloadListCell(CompletedDownloadController.this);
            }
            
        });

        loadCompletedList();
    }
    
    private void loadCompletedList() {
        try {
            downloadInfoObservableList.addAll(dbHandler.getList());
        } catch (Exception e) {
            System.err.println(getClass().getName()+" loadCompletedList :"+e.getMessage());
        }  
    }
    
    @Override
    public void onBtnEventOccured(int index, BtnEventType eventType) {
       switch(eventType){
            case FILE_OPEN_EVENT:
                DownloadInfo data = downloadInfoObservableList.get(index);
                openFolder(data.getStorageLocation());
                //System.out.println("Opening folder :"+ data.getStorageLocation());
                break;
            case DELETE_EVENT:
                onDelete(index);
                break;  
            default:
                break;
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


    public void openFolder(String storageLocation) {
        
        if (Desktop.isDesktopSupported()) {
            new Thread(() -> {
                try {
                    File file = new File (storageLocation);
                    Desktop.getDesktop().open(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }else{
            //Todo: use dialog to notify that open folder not possible for this os.
            System.out.println(getClass().getName()+" openFolder: Can't open folder for this os");
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()){
            case Config.INSERTION_SUCCESS_NOTIFICATION:
                onGettingNewData(evt.getNewValue());
                break;
        }
        
    }


    private void onGettingNewData(Object newValue) {
        try {
            DownloadInfo data = (DownloadInfo) newValue;
            downloadInfoObservableList.add(data);
        } catch (Exception e) {
            System.err.println(getClass().getName()+" onGettingNewData :"+e.getMessage());
        }
    }


    @Override
    public String getWindowTitle() {
        return com.rdas6313.Config.COMPLETED_DOWNLOAD_WINDOW_TITLE;
    }

    @Override
    protected String getFxmlPath() {
       return com.rdas6313.Config.LOAD_COMPLETED_DOWNLOAD;
    }



    
    
}
