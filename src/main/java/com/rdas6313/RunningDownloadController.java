package com.rdas6313;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.rdas6313.ApiConnection.DataCodes;
import com.rdas6313.ApiConnection.Request;
import com.rdas6313.ApiConnection.ResponseCodes;
import com.rdas6313.DataBase.DbHandler;

import org.json.simple.JSONObject;

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
    
    private Request downloadRequest;
    
    public RunningDownloadController(DbHandler[] dbHandler, Request downloadRequest) {
        this.dbHandler = dbHandler;
        this.downloadRequest = downloadRequest;
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
        switch (eventType) {
            case PAUSED_EVENT:
                onPause(index);
                break;
            default:
                break;
        }
    }

    

    private void onPause(int index) {
        //Pause Button Clicked remove from Download list and save into database's paused list table
        try {
            DownloadInfo info = downloadInfoObservableList.get(index);
            downloadRequest.stopDownload(info.getId());
            downloadInfoObservableList.remove(info);
            dbHandler[0].insert(info);
        }catch(NullPointerException e){
            System.err.println(getClass().getSimpleName()+" onPause: "+e.getMessage());
        } catch (Exception e) {
            System.err.println(getClass().getSimpleName()+" onPause: "+e.getMessage());
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch(evt.getPropertyName()){
            case Config.ITEM_ADDED_RUNNING_DOWNLOAD_NOTIFICATION:
                System.out.println(getClass().getSimpleName()+" ITEM ADDED: ");
                onItemAdded(evt.getNewValue());
                break;
            case ResponseCodes.ON_COMPLETE:
                onComplete(evt.getNewValue());
                break;
            case ResponseCodes.ON_ERROR:
                onError(evt.getNewValue());
                break;
            case ResponseCodes.ON_PROGRESS:
                onProgress(evt.getNewValue());
                break;
            case ResponseCodes.ON_START_DOWNLOAD:
               // onStartDownload(evt.getNewValue());
                break;
            case ResponseCodes.ON_STOP_DOWNLOAD:
                //onStopDownload(evt.getNewValue());
                break;
            default:
                break;
        }
        
    }

/*     private void onStopDownload(Object newValue) {
        try {
            if(!(newValue instanceof JSONObject))
                throw new IllegalArgumentException("newValue is not JSONObject");
            JSONObject data = (JSONObject) newValue;
            int id = (int)data.get(DataCodes.DOWNLOAD_ID);
            long downloadedSize = (long)data.get(DataCodes.DOWNLOADED_SIZE);
            DownloadInfo info = getDownloadFromList(id,pausedDataList);
            info.setCurrentSize(downloadedSize);
            pausedDataList.remove(info);
            dbHandler[0].insert(info);
        }catch(IllegalArgumentException e){
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }catch(NullPointerException e){
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }catch (Exception e) {
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }
    } */

    /* private void onStartDownload(Object newValue) {
        try {
            if(!(newValue instanceof JSONObject))
                throw new IllegalArgumentException("newValue is not JSONObject");
            JSONObject data = (JSONObject) newValue;
            int id = (int)data.get(DataCodes.DOWNLOAD_ID);
            String filename = (String)data.get(DataCodes.FILE_NAME);
            long size = (long)data.get(DataCodes.FILE_SIZE);
            String url = (String)data.get(DataCodes.URL);
            String storageLocation = (String)data.get(DataCodes.SAVED_LOCATION);
            DownloadInfo info = new DownloadInfo(url, filename, storageLocation, id, size);
            downloadInfoObservableList.add(info);
        }catch(IllegalArgumentException e){
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }catch(NullPointerException e){
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }catch (Exception e) {
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }
    } */

    private void onProgress(Object newValue) {
  
        try {
            if(!(newValue instanceof JSONObject)){
                throw new IllegalArgumentException("Illegal argument given in onProgress");
            }
            JSONObject data = (JSONObject) newValue;
            int download_id = (int)data.get(DataCodes.DOWNLOAD_ID);
            long downloadedSize = (long)data.get(DataCodes.DOWNLOADED_SIZE);
            DownloadInfo info = getDownloadFromList(download_id,downloadInfoObservableList);
            info.setCurrentSize(downloadedSize);
            
        }catch(IllegalArgumentException e){
            System.err.println(getClass().getName()+" onProgress : "+e.getMessage());
        }catch(NullPointerException e){
            System.err.println(getClass().getName()+" onProgress : "+e.getMessage());
        } catch (Exception e) {
            System.err.println(getClass().getName()+" onProgress : "+e.getMessage());
        }  

    }

    private void onError(Object newValue) {
        try {
            if(!(newValue instanceof JSONObject)){
                throw new IllegalArgumentException("Illegal argument given in onError");
            }
            JSONObject data = (JSONObject) newValue;
            int download_id = (int)data.get(DataCodes.DOWNLOAD_ID);
            DownloadInfo info = getDownloadFromList(download_id,downloadInfoObservableList);
            downloadInfoObservableList.remove(info);
            dbHandler[2].insert(info);    
        }catch(IllegalArgumentException e){
            System.err.println(getClass().getName()+" onError : "+e.getMessage());
        }catch(NullPointerException e){
            System.err.println(getClass().getName()+" onError : "+e.getMessage());
        } catch (Exception e) {
            System.err.println(getClass().getName()+" onError : "+e.getMessage());
        }  
    }

    private void onComplete(Object newValue) {
        
        try {
            if(!(newValue instanceof JSONObject)){
                throw new IllegalArgumentException("Illegal argument given in onComplete");
            }
            JSONObject data = (JSONObject) newValue;
            int download_id = (int)data.get(DataCodes.DOWNLOAD_ID);
            DownloadInfo info = getDownloadFromList(download_id,downloadInfoObservableList);
            downloadInfoObservableList.remove(info);
            if(info == null)
                System.err.println(getClass().getSimpleName()+" onComplete: getting no object from download list");
            info.setFileSize(info.getCurrentSize());
            dbHandler[1].insert(info);
            
        }catch(IllegalArgumentException e){
            System.err.println(getClass().getName()+" onComplete : "+e.getMessage());
            e.printStackTrace();
        }catch(NullPointerException e){
            System.err.println(getClass().getName()+" onComplete : "+e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println(getClass().getName()+" onComplete : "+e.getMessage());
            e.printStackTrace();
        }  
    }

    private DownloadInfo getDownloadFromList(int download_id,List<DownloadInfo> list) throws Exception{
        for(DownloadInfo info: list){
            if(info.getId() == download_id){
                return info;
            }
        }
        return null;
        //throw new Exception("DownloadInfo object not found with this download key "+download_id);
    }

    private void onItemAdded(Object newValue) {

        try {
            if(!(newValue instanceof DownloadInfo)){
                throw new IllegalArgumentException("Argument passed in onItemAdded is not a DownloadInfo object");
            }
            DownloadInfo info = (DownloadInfo)newValue;
            downloadInfoObservableList.add(info);
        } catch(NullPointerException e){
            System.err.println(getClass().getSimpleName()+" onItemAdded: "+Config.OBSERVABLE_LIST_EXCEPTION_MSG);
        }catch (Exception e) {
            System.err.println(getClass().getSimpleName()+" onItemAdded: "+e.getMessage());
        }
    }
    
    
}
