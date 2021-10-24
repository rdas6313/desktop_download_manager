package com.rdas6313;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import com.rdas6313.DataBase.DbHandler;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class RunningDownloadController extends TitleController implements Initializable,BtnEventNotifiable,PropertyChangeListener{

    @FXML
    private AnchorPane rootView;

    @FXML
    private ListView<DownloadInfo> listView;

    private ObservableList<DownloadInfo> downloadInfoObservableList = FXCollections.observableArrayList();;

    private DbHandler dbHandler;

    public RunningDownloadController(DbHandler dbHandler) {
        this.dbHandler = dbHandler;
        
    }

    public RunningDownloadController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setItems(downloadInfoObservableList);
        listView.setOrientation(Orientation.VERTICAL);
        listView.setCellFactory(new Callback<ListView<DownloadInfo>,ListCell<DownloadInfo>>(){

            @Override
            public ListCell<DownloadInfo> call(ListView<DownloadInfo> param) {
                
                return new RunningDownloadListCell(RunningDownloadController.this);
            }
            
        });
        
    }

    

    @Override
    public String getWindowTitle() {
        return Config.RUNNING_DOWNLOAD_WINDOW_TITLE;
    }

    @Override
    protected String getFxmlPath() {
        return Config.LOAD_RUNNING_DOWNLOAD;
    }

    

    @Override
    public void onBtnEventOccured(int index,BtnEventType eventType) {
        if(eventType != BtnEventType.PAUSED_EVENT)
            return;
            
      //Pause Button Clicked remove from Download list and save into database's paused list table
        DownloadInfo data = downloadInfoObservableList.remove(index);
        try {
            dbHandler.insert(data);
        } catch (NullPointerException e) {
            System.err.println(getClass().getName()+" onbuttonClick : "+e.getMessage());
        }
    }

    

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        DownloadInfo info = (DownloadInfo)evt.getNewValue();
        
        if(downloadInfoObservableList == null)
            throw new NullPointerException(Config.OBSERVABLE_LIST_EXCEPTION_MSG);
        else if(info == null)
            throw new NullPointerException(Config.DOWNLOAD_INFO_EXCEPTION_MSG);
        
        downloadInfoObservableList.add(info);
        
        //Comment this function if test is done
        testSet();
        
    }

   
    
    private void testSet(){
        //downloadInfoObservableList.add(new DownloadInfo("awe", "Tum hi ho.mp3", " ", 1, 1024*1024));
        Thread thread = new Thread(new Runnable(){

            @Override
            public void run() {
                for(int i=0;i<1024;i++){
                    
                    for(int j=0;j<downloadInfoObservableList.size();j++){
                        DownloadInfo info = downloadInfoObservableList.get(j);
                        Platform.runLater(new Runnable(){

                            @Override
                            public void run() {
                                info.setCurrentSize(info.getCurrentSize()+1024);                            
                            }
                            
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                
            }
            
        });
        thread.start();
    }

        
    
    
}
