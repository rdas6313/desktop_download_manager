package com.rdas6313;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class DrawerController extends Controller implements Initializable,EventHandler<ActionEvent>{


    @FXML
    private JFXButton addDownloadBtn;

    @FXML
    private JFXButton ongoingDownloadBtn;

    @FXML
    private JFXButton pausedDownloadBtn;

    @FXML
    private JFXButton errorDownloadBtn;

    @FXML
    private JFXButton completedDownloadBtn;

    @FXML
    private JFXButton settingsBtn;




    public DrawerController() {
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDownloadBtn.setOnAction(this);
        ongoingDownloadBtn.setOnAction(this);
        pausedDownloadBtn.setOnAction(this);
        errorDownloadBtn.setOnAction(this);
        completedDownloadBtn.setOnAction(this);
        settingsBtn.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent event) {
       JFXButton button = (JFXButton)event.getSource();
       notifyObservers("SIDEBAR_BTN_CLICK", null, button.getId());
    }

    

    @Override
    protected String getFxmlPath() {
        return Config.LOAD_NAV_DRAWER;
    }
    

}
