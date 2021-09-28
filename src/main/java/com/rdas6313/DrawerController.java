package com.rdas6313;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

public class DrawerController implements Initializable,Loadable,EventHandler<ActionEvent>{


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

    private ClickNotifiable clickNotifiable;


    public DrawerController(ClickNotifiable clickNotifiable) {
        this.clickNotifiable = clickNotifiable;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDownloadBtn.setOnAction(this);
        
    }

    @Override
    public void handle(ActionEvent event) {
       JFXButton button = (JFXButton)event.getSource();
       switch(button.getId()){
           case "addDownloadBtn":
                clickNotifiable.onbuttonClick(Config.ADD_DOWNLOAD_BUTTON_ID);
                break;
        
       }
        
    }



    @Override
    public Parent loadFxml() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Config.LOAD_NAV_DRAWER));
        fxmlLoader.setController(this);
        return fxmlLoader.load();
    }
    

}
