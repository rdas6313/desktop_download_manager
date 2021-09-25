package com.rdas6313;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

public class DrawerController implements Initializable,Loadable{

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
    private void onClickAddBtn(ActionEvent event) {
        System.out.println("Its working");
        
    }

    @Override
    public Parent loadFxml() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Config.LOAD_NAV_DRAWER));
        fxmlLoader.setController(this);
        return fxmlLoader.load();
    }
    

}
