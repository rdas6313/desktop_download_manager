package com.rdas6313;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import org.apache.commons.validator.routines.UrlValidator;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class AddDownloadController implements Initializable,Loadable,EventHandler<ActionEvent>,ChangeListener<String>{
    

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
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        infoDownloadBtn.setOnAction(this);
        fileChooserBtn.setOnAction(this);
        downloadStartBtn.setOnAction(this);
        progressIndicator.setVisible(false);
        isInfoAvailable = false;
        urlTextField.textProperty().addListener(this);
    }

    @Override
    public Parent loadFxml() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Config.LOAD_ADD_DOWNLOAD_WINDOW));
        fxmlLoader.setController(this);
        return fxmlLoader.load();
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
        fileChooseErrorIndicator.setVisible(false);
        urlErrorIndicator.setVisible(false);
        filenameErrorIndicator.setVisible(false);
        errorLabel.setVisible(false);

        String url = urlTextField.getText();
        String saveLocation = fileChooserTextField.getText();
        String filename = fileNameTextField.getText();
        if(saveLocation == null || saveLocation.isEmpty()){
            fileChooseErrorIndicator.setVisible(true);
            errorLabel.setText(Config.EMPTY_FILE_PATH_MSG);
            errorLabel.setVisible(true);
            return;
        }else if(!isInfoAvailable){
            urlErrorIndicator.setVisible(true);
            errorLabel.setText(Config.GET_INFO_MSG);
            errorLabel.setVisible(true);
            return;
        }else if(filename == null || filename.isEmpty()){
            filenameErrorIndicator.setVisible(true);
            errorLabel.setText(Config.FILE_NAME_ERROR);
            errorLabel.setVisible(true);
            return;
        }else if(url.isEmpty()){
            urlErrorIndicator.setVisible(true);
            errorLabel.setText(Config.URL_ERROR_MSG);
            errorLabel.setVisible(true);
            return;
        }

        //make download request and redirect to running download list page..
        makeDownloadRequest();

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

    private void makeDownloadRequest() {
        //make download request and redirect to running download list page..
        showDialog();
    }

    private void onClickFileChooser(ActionEvent event) {
        Parent par = (Parent)event.getSource();
        Stage stage = (Stage)(par.getScene().getWindow());
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if(selectedDirectory == null || selectedDirectory.getAbsolutePath().isEmpty()){
            fileChooseErrorIndicator.setVisible(true);
            errorLabel.setText(Config.EMPTY_FILE_PATH_MSG);
            errorLabel.setVisible(true);
            return;
        }
        fileChooserTextField.setText(selectedDirectory.getAbsolutePath());
        fileChooseErrorIndicator.setVisible(false);
        errorLabel.setVisible(false);
    }

    private void onClickInfoDownloadBtn() {
        urlErrorIndicator.setVisible(false);
        errorLabel.setVisible(false);
        String data = urlTextField.getText();
        
        if(data.isEmpty() || !isValidUrl(data)){
            urlErrorIndicator.setVisible(true);
            errorLabel.setText(Config.URL_ERROR_MSG);
            errorLabel.setVisible(true);
            return;
        }
        progressIndicator.setVisible(true);

        // send request to Download Api for file info
        testDataFetching();
        // if you got download info then make isInfoAvailable = true
    }

    private void testDataFetching(){ // test function , remove when test is done.

        Timer timer = new Timer();
        timer.schedule(new TimerTask(){

            @Override
            public void run() {
                Platform.runLater(() -> {
                    progressIndicator.setVisible(false);
                    fileNameTextField.setText("Pal by arijit singh.mp3");
                    sizeLabel.setText("Size 5 MB");
                    isInfoAvailable = true;
                
                });
                
            }
            
        }, 2000);

        
    }

    private boolean isValidUrl(String data) {
        UrlValidator validator = new UrlValidator();
        return validator.isValid(data);
    }



    
    
}
