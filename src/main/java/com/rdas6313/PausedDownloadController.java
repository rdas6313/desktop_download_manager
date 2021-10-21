package com.rdas6313;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import com.rdas6313.DataBase.DbHandler;
import com.rdas6313.DataBase.Config;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class PausedDownloadController extends TitleController implements ClickNotifiable,Initializable,PropertyChangeListener{
    
    
    @FXML
    private AnchorPane rootView;
    
    @FXML
    private ListView<DownloadInfo> listView;
    
    private ObservableList<DownloadInfo> downloadInfoObservableList = FXCollections.observableArrayList();;
    
    private DbHandler dbHandler;
    
    public PausedDownloadController(DbHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
        listView.setItems(downloadInfoObservableList);
        listView.setOrientation(Orientation.VERTICAL);
        listView.setCellFactory(new Callback<ListView<DownloadInfo>,ListCell<DownloadInfo>>(){

            @Override
            public ListCell<DownloadInfo> call(ListView<DownloadInfo> param) {
                
                return new PausedDownloadListCell(PausedDownloadController.this);
            }
            
        });

        loadDownloadList();
    }

    
    @Override
    public void onbuttonClick(int index) {
        try {
            DownloadInfo data = (DownloadInfo) downloadInfoObservableList.remove(index);
            dbHandler.delete(data.getId());
            makeDownloadRequest(data);
            notifyObservers(com.rdas6313.Config.ADD_DOWNLOAD_NOTIFICATION, null, data);
        } catch (Exception e) {
            System.err.println(getClass().getName()+" onButtonClick :"+e.getMessage());
        }
    }

    private void makeDownloadRequest(DownloadInfo data) {
        //Todo: make download request here
    }

    @Override
    public String getWindowTitle() {
        return com.rdas6313.Config.PAUSED_DOWNLOAD_TITLE;
    }
    
    @Override
    protected String getFxmlPath() {
        return com.rdas6313.Config.LOAD_PAUSED_DOWNLOAD;
    }
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()){
            case Config.INSERTION_SUCCESS_NOTIFICATION:
                downloadInfoObservableList.add((DownloadInfo)evt.getNewValue());
                break;
        }
        
    }

    private void loadDownloadList(){
        try {
            downloadInfoObservableList.addAll(dbHandler.getList());
        } catch (Exception e) {
            System.err.println(getClass().getName()+" loadDownloadList :"+e.getMessage());
        }    
    }
    
}
