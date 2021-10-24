package com.rdas6313;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import com.jfoenix.controls.JFXButton;

public class RunningDownloadListCell extends ListCell<DownloadInfo>{
    
    @FXML
    private Label filenameLabel;
    
    @FXML
    private ProgressBar progressBar;
    
    @FXML
    private Label sizeLabel;
    
    @FXML
    private JFXButton pauseBtn;
    
    @FXML
    private AnchorPane anchorPane;
    
    private FXMLLoader mLoader;
    
    private BtnEventNotifiable clickNotifiable;
    
    public RunningDownloadListCell(BtnEventNotifiable clickNotifiable) {
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
                mLoader = new FXMLLoader(getClass().getResource(Config.LOAD_RUNNING_LIST_ITEM));
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
            pauseBtn.setOnAction((ActionEvent event)->{
                clickNotifiable.onBtnEventOccured(getIndex(),BtnEventType.PAUSED_EVENT);
            });
            
            setText(null);
            setGraphic(anchorPane);
        }
    }
    
    
    
    
    
}
