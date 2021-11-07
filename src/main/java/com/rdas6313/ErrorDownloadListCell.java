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

public class ErrorDownloadListCell extends ListCell<DownloadInfo>{

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label filenameLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label sizeLabel;

    @FXML
    private JFXButton deleteBtn;
    
    private BtnEventNotifiable clickNotifiable;

    
    private FXMLLoader mLoader;
    
    public ErrorDownloadListCell(BtnEventNotifiable clickNotifiable) {
        this.clickNotifiable = clickNotifiable;
    }

    @Override
    protected void updateItem(DownloadInfo item, boolean empty) {
        // TODO Auto-generated method stub
        super.updateItem(item, empty);
        if(empty || item == null) {
            
            setText(null);
            setGraphic(null);
            
        } else {
            if (mLoader == null) {
                mLoader = new FXMLLoader(getClass().getResource(Config.LOAD_ERROR_LIST_ITEM));
                mLoader.setController(this);
                
                try {
                    mLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
            
            filenameLabel.setText(item.getFilename());
            sizeLabel.setText(Helper.formatTextForSizeAndProgress(
                item.getCurrentSize(), 
                item.getSize(), 
                Helper.calculateProgress(
                    item.getCurrentSize(), 
                    item.getSize()
                )
            ));
            progressBar.setProgress(Helper.calculateProgress(item.getCurrentSize(), item.getSize()));
            
            deleteBtn.setOnAction((ActionEvent event)->{
                clickNotifiable.onBtnEventOccured(getIndex(),BtnEventType.DELETE_EVENT);
            });

            setText(null);
            setGraphic(anchorPane);
        }
    }
    
}
