package com.rdas6313;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import com.rdas6313.DataBase.DbHandler;

import org.json.simple.JSONObject;

import com.rdas6313.ApiConnection.DataCodes;
import com.rdas6313.ApiConnection.Request;
import com.rdas6313.ApiConnection.ResponseCodes;
import com.rdas6313.DataBase.DbConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class PausedDownloadController extends TitleController implements BtnEventNotifiable,Initializable,PropertyChangeListener{
    
    
    @FXML
    private AnchorPane rootView;
    
    @FXML
    private ListView<DownloadInfo> listView;
    
    private ObservableList<DownloadInfo> downloadInfoObservableList;
    
    private DbHandler dbHandler;
    private Request downloRequest;
    
    

    public PausedDownloadController(DbHandler dbHandler, Request downloRequest) {
        this.dbHandler = dbHandler;
        this.downloRequest = downloRequest;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        downloadInfoObservableList = FXCollections.observableArrayList();
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
    public void onBtnEventOccured(int index,BtnEventType eventType) {
        switch(eventType){
            case RESUME_EVENT:
                onResume(index);
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

    private void onResume(int index) {
        
         try {
            DownloadInfo data = (DownloadInfo) downloadInfoObservableList.remove(index);
            dbHandler.delete(data.getId());
            int id = downloRequest.startDownload(data.getUrl(), data.getFilename(), data.getStorageLocation());
            DownloadInfo info = new DownloadInfo(data.getUrl(), data.getFilename(), data.getStorageLocation(), id, data.getSize(),data.getCurrentSize());
            notifyObservers(com.rdas6313.Config.ADD_DOWNLOAD_NOTIFICATION, null, info);
        }catch(NullPointerException e){
            System.err.println(getClass().getName()+" onResume Method: "+e.getMessage());
        } catch (Exception e) {
            System.err.println(getClass().getName()+" onResume Method:"+e.getMessage());
        }
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
            case DbConfig.INSERTION_SUCCESS_NOTIFICATION:
                onInsertion((DownloadInfo)evt.getNewValue());
                break;
            case DbConfig.DELETION_ERROR_NOTIFICATION:
                System.out.println("Unable to delete"); //Todo: show dialog here.
                break;
            case ResponseCodes.ON_START_DOWNLOAD:
                //onStartDownload(evt.getNewValue());
                break;
        }
        
    }

    private void onInsertion(DownloadInfo newValue) {
        try {
            if(downloadInfoObservableList == null)
                return;
            downloadInfoObservableList.add(newValue);
        } catch (Exception e) {
            System.err.println(getClass().getSimpleName()+" onInsertion: "+e.getMessage());
        }
    }


   /*  private void onStartDownload(Object newValue) {
        try{
            JSONObject data = (JSONObject) newValue;
            int id = (int)data.get(DataCodes.DOWNLOAD_ID);
            String filename = (String)data.get(DataCodes.FILE_NAME);
            long size = (long)data.get(DataCodes.FILE_SIZE);
            String url = (String)data.get(DataCodes.URL);
            long downloadedSize = (long)data.get(DataCodes.DOWNLOADED_SIZE);
            String storageLocation = (String)data.get(DataCodes.SAVED_LOCATION);
            DownloadInfo info = new DownloadInfo(url, filename, storageLocation, id, size,downloadedSize);
            notifyObservers(com.rdas6313.Config.ADD_DOWNLOAD_NOTIFICATION, null, info);
            System.out.println(getClass().getSimpleName()+" onStartDownload: calling");
        }catch(NullPointerException e){
            System.err.println(getClass().getName()+" onStartDownload :"+e.getMessage());
        }catch(Exception e){
            System.err.println(getClass().getName()+" onStartDownload :"+e.getMessage());
        }
    } */


    private void loadDownloadList(){
        try {
            downloadInfoObservableList.addAll(dbHandler.getList());
        } catch (Exception e) {
            System.err.println(getClass().getName()+" loadDownloadList :"+e.getMessage());
        }    
    }
    
}
