package com.rdas6313;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class PausedDownloadListCell extends ListCell<DownloadInfo>{

    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label filenameLabel;
    
    @FXML
    private ProgressBar progressBar;
    
    @FXML
    private Label sizeLabel;
    
    @FXML
    private JFXButton playBtn;
    
    private FXMLLoader mLoader;
    
    private ClickNotifiable clickNotifiable;
    
    public PausedDownloadListCell(ClickNotifiable clickNotifiable) {
        this.clickNotifiable = clickNotifiable;
    }
    
    @Override
    protected void updateItem(DownloadInfo item, boolean empty) {
        super.updateItem(item, empty);
         
        if(empty || item == null) {
            
            setText(null);
            setGraphic(null);
            
        } else {
            if (mLoader == null) {
                mLoader = new FXMLLoader(getClass().getResource(Config.LOAD_PAUSED_LIST_ITEM));
                mLoader.setController(this);
                
                try {
                    mLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
            
            filenameLabel.setText(item.getFilename());
            sizeLabel.textProperty().bind(item.getSizeAndProgressProperty());
            progressBar.progressProperty().bind(item.getProgressProperty());
            playBtn.setOnAction((ActionEvent event)->{
                clickNotifiable.onbuttonClick(getIndex());
            });
            
            setText(null);
            setGraphic(anchorPane);
        }
    }
    
    
}
