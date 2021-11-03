package com.rdas6313;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.rdas6313.ApiConnection.DataCodes;
import com.rdas6313.ApiConnection.Request;
import com.rdas6313.ApiConnection.ResponseCodes;

import org.apache.commons.validator.routines.UrlValidator;
import org.json.simple.JSONObject;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

public class AddDownloadController extends TitleController implements Initializable,EventHandler<ActionEvent>,ChangeListener<String>,PropertyChangeListener{
    

    @FXML
    private TextField urlTextField;

    @FXML
    private JFXButton infoDownloadBtn;

    @FXML
    private TextField fileChooserTextField;

    @FXML
    private JFXButton fileChooserBtn;

    @FXML
    private Label sizeLabel;

    @FXML
    private JFXButton downloadStartBtn;

    @FXML
    private Circle urlErrorIndicator;

    @FXML
    private Circle fileChooseErrorIndicator;

    @FXML
    private Circle filenameErrorIndicator;

    @FXML
    private TextField fileNameTextField;

    @FXML
    private Label errorLabel;

    
    @FXML
    private ProgressIndicator progressIndicator;
    
    @FXML
    private StackPane stackPane;
    
    private boolean isInfoAvailable;
    
    private UrlValidator validator;
    
    private Request downloadRequest;
    
    public AddDownloadController(Request downloadRequest) {
        this.downloadRequest = downloadRequest;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        infoDownloadBtn.setOnAction(this);
        fileChooserBtn.setOnAction(this);
        downloadStartBtn.setOnAction(this);
        progressIndicator.setVisible(false);
        isInfoAvailable = false;
        urlTextField.textProperty().addListener(this);
        validator = new UrlValidator();
    }

    @Override
    public String getWindowTitle() {
        return Config.ADD_DOWNLOAD_WINDOW_TITLE;
    }

    @Override
    protected String getFxmlPath() {
        return Config.LOAD_ADD_DOWNLOAD_WINDOW;
    }


    @Override
    public void handle(ActionEvent event) {
        JFXButton button = (JFXButton)event.getSource();
        switch(button.getId()){
            case "infoDownloadBtn":
                onClickInfoDownloadBtn();
                break;
            case "fileChooserBtn":
                onClickFileChooser(event);
                break;
            case "downloadStartBtn":
                onClickDownloadBtn();
                break;
        }
        
    }

   

    @Override
    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        isInfoAvailable = false;
        
    }

    private void onClickDownloadBtn() {
        //on click download button what will happen
        unSetErrors();
        
        String url = urlTextField.getText();
        String saveLocation = fileChooserTextField.getText();
        String filename = fileNameTextField.getText();

        if(saveLocation == null || saveLocation.isEmpty()){
            setFileChooseError(Config.EMPTY_FILE_PATH_MSG);
            return;
        }else if(!isInfoAvailable){
            setUrlError(Config.GET_INFO_MSG);
            return;
        }else if(filename == null || filename.isEmpty()){
            setFileNameError(Config.FILE_NAME_ERROR);
            return;
        }else if(url.isEmpty()){
            setUrlError(Config.URL_ERROR_MSG);
            return;
        }

        makeDownloadRequest(url,filename,saveLocation);

    }

    private void setFileNameError(String msg) {
        filenameErrorIndicator.setVisible(true);
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }



    private void setUrlError(String msg) {
        urlErrorIndicator.setVisible(true);
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }



    private void setFileChooseError(String msg) {
        fileChooseErrorIndicator.setVisible(true);
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }



    private void unSetErrors(){
        fileChooseErrorIndicator.setVisible(false);
        urlErrorIndicator.setVisible(false);
        filenameErrorIndicator.setVisible(false);
        errorLabel.setVisible(false);
    }

    private void showDialog(){
        JFXDialogLayout content= new JFXDialogLayout();
        Text headingText = new Text(Config.DOWNLOAD_ADDED_HEADER_MSG);
        headingText.setFill(Color.GREEN);
        content.setHeading(headingText);
        content.setBody(new Text(Config.DOWNLOAD_ADDED_BODY_MSG));
        
        JFXDialog dialog =new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button=new JFXButton(Config.DIALOG_BTN_TEXT);
        button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                dialog.close();
            }
        });
        content.setActions(button);
        dialog.show();
    }

    private void makeDownloadRequest(String url,String filename,String storageLocation) {
        //make download request and redirect to running download list page..
        if(downloadRequest == null){
            System.out.println(getClass().getSimpleName()+" makeDownloadRequest: null downloadRequest");
            return;
        }
        downloadRequest.startDownload(url, filename, storageLocation);
    }

    private void onClickFileChooser(ActionEvent event) {
        Parent par = (Parent)event.getSource();
        Stage stage = (Stage)(par.getScene().getWindow());
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if(selectedDirectory == null || selectedDirectory.getAbsolutePath().isEmpty()){
            setFileChooseError(Config.EMPTY_FILE_PATH_MSG);
            return;
        }
        fileChooserTextField.setText(selectedDirectory.getAbsolutePath());
        unSetErrors();

    }

    private void onClickInfoDownloadBtn() {
        
        unSetErrors();
        
        String urlData = urlTextField.getText();

        if(!validator.isValid(urlData)){
            setUrlError(Config.URL_ERROR_MSG);
            return;
        }
        progressIndicator.setVisible(true);

        // send request to Download Api for file info
        if(downloadRequest == null){
            System.out.println(getClass().getSimpleName()+" onClickInfoDownloadBtn: Download Request object is null");
            return;
        }
        downloadRequest.info(urlData);
        // if you got download info then make isInfoAvailable = true
    }

    

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case ResponseCodes.ON_INFO:
                onInfo(evt.getNewValue());
                break;
            case ResponseCodes.ON_START_DOWNLOAD:
                onStartDownload(evt.getNewValue());
                break;
            default:
                break;
        }
        
    }

    private void onStartDownload(Object newValue) {
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
            notifyObservers(Config.ADD_DOWNLOAD_NOTIFICATION, null, info);
            showDialog();
        }catch(IllegalArgumentException e){
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }catch(NullPointerException e){
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }catch (Exception e) {
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }
    }

    private void onInfo(Object newValue) {
        try {
            if(!(newValue instanceof JSONObject))
                throw new IllegalArgumentException("newValue is not JSONObject");
            JSONObject data = (JSONObject) newValue;
            String fileName = (String)data.get(DataCodes.FILE_NAME);
            long size = (long)data.get(DataCodes.FILE_SIZE);
            progressIndicator.setVisible(false);
            fileNameTextField.setText(fileName);
            sizeLabel.setText("Size "+Helper.calculateSizeInText(size));
            isInfoAvailable = true;
        } catch(IllegalArgumentException e){
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }catch(NullPointerException e){
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }catch (Exception e) {
            System.err.println(getClass().getSimpleName()+" onInfo: "+e.getMessage());
        }
        
    }
    
}
