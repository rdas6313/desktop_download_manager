package com.rdas6313;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class DrawerController implements Initializable{

    @FXML
    private JFXButton addDownloadBtn;

    @FXML
    private JFXButton ongoingDownloadBtn;

    @FXML
    private JFXButton pausedDownloadBtn;

    @FXML
    private JFXButton pausedDownloadBtn1;

    @FXML
    private JFXButton settingsBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    void onClickAddBtn(ActionEvent event) {
        System.out.println("Its working");
        
    }
    

}
