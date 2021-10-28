package com.rdas6313;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
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

    private DbHandler[] dbHandler;

    public RunningDownloadController(DbHandler[] dbHandler) {
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
            dbHandler[0].insert(data);
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

    private void insertIntoDb(DownloadInfo info) {
        

        Platform.runLater(new Runnable(){

            @Override
            public void run() {
                try {
                    downloadInfoObservableList.remove(info);
                    dbHandler[1].insert(info);
                    
                } catch (Exception e) {
                    System.err.println(getClass().getName()+" insertIntoDb : "+e.getMessage());
                }                 
            }
        });
    }
   
    
    private void testSet(){
        //downloadInfoObservableList.add(new DownloadInfo("awe", "Tum hi ho.mp3", " ", 1, 1024*1024));

        Thread thread = new Thread(new Runnable(){

            @Override
            public void run() {
               int downloadedData = 512;
               int i = 0;
                while(downloadInfoObservableList.size() > 0){
                    DownloadInfo info = downloadInfoObservableList.get(i);
                    
                    if(info.getCurrentSize() < info.getSize()){
                        increaseSize(info, downloadedData);
                    }else{
                        insertIntoDb(info);
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(downloadInfoObservableList.size() > 0)
                        i = ((i+1)%downloadInfoObservableList.size());
               }
                
            }
            
        });

        thread.start();

    }

    private void increaseSize(DownloadInfo info,int downloadedData){
        Platform.runLater(new Runnable(){

            @Override
            public void run() {
                info.setCurrentSize(info.getCurrentSize()+downloadedData);         
            }
        });
    }  
    
    
}
