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

public class CompletedDownloadListCell extends ListCell<DownloadInfo>{

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label filenameLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label sizeLabel;

    @FXML
    private JFXButton fileOpenBtn;

    @FXML
    private JFXButton deleteBtn;
    
    private BtnEventNotifiable clickNotifiable;

    private FXMLLoader mLoader;
    
    public CompletedDownloadListCell(BtnEventNotifiable clickNotifiable) {
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
                mLoader = new FXMLLoader(getClass().getResource(Config.LOAD_COMPLETED_LIST_ITEM));
                mLoader.setController(this);
                
                try {
                    mLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
            }
            
            filenameLabel.setText(item.getFilename());
            sizeLabel.setText(Helper.formatTextForSizeAndProgress(
                item.getSize(), 
                item.getSize(), 
                Helper.calculateProgress(
                    item.getSize(), 
                    item.getSize()
                )
            ));
           progressBar.setProgress(Helper.calculateProgress(item.getSize(), item.getSize()));
            fileOpenBtn.setOnAction((ActionEvent event)->{
                clickNotifiable.onBtnEventOccured(getIndex(),BtnEventType.FILE_OPEN_EVENT);
            });
            
            deleteBtn.setOnAction((ActionEvent event)->{
                clickNotifiable.onBtnEventOccured(getIndex(),BtnEventType.DELETE_EVENT);
            });

            setText(null);
            setGraphic(anchorPane);
        }
    }
    
}
