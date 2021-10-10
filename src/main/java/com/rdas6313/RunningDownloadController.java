package com.rdas6313;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class RunningDownloadController implements Initializable,Loadable,DownloadEvent,ClickNotifiable{

    @FXML
    private AnchorPane rootView;

    @FXML
    private ListView<DownloadInfo> listView;

    private ObservableList<DownloadInfo> downloadInfoObservableList;

    public RunningDownloadController() {
        downloadInfoObservableList = FXCollections.observableArrayList();

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setItems(downloadInfoObservableList);
        listView.setOrientation(Orientation.VERTICAL);
        listView.setCellFactory(new Callback<ListView<DownloadInfo>,ListCell<DownloadInfo>>(){

            @Override
            public ListCell<DownloadInfo> call(ListView<DownloadInfo> param) {
                
                return new DownloadListViewCell(RunningDownloadController.this);
            }
            
        });
        
    }

    @Override
    public Parent loadFxml() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Config.LOAD_RUNNING_DOWNLOAD));
        fxmlLoader.setController(this);
        return fxmlLoader.load();
    }

    

    @Override
    public void onbuttonClick(int index) {
      //Pause Button Clicked remove from Download list and save into database's paused list table
      downloadInfoObservableList.remove(index);
        
    }

    @Override
    public void onAdd(DownloadInfo info) {
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
