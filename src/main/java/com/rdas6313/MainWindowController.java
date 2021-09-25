package com.rdas6313;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MainWindowController implements Initializable{

    @FXML
    private JFXHamburger hambargerBtn;

    @FXML
    private Label windowTitle;

    @FXML
    private JFXDrawer navDrawer;

    @FXML
    private Pane paneWindow;

    @FXML
    private Label testLabel;
    
    private HamburgerBackArrowBasicTransition transition;

    private AnchorPane sideDrawer;



    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            sideDrawer = FXMLLoader.load(getClass().getResource(Config.LOAD_NAV_DRAWER));
            navDrawer.setSidePane(sideDrawer);
            sideDrawer.prefWidthProperty().bind(navDrawer.widthProperty());
            sideDrawer.prefHeightProperty().bind(navDrawer.heightProperty());
            transition = new HamburgerBackArrowBasicTransition(hambargerBtn);
            transition.setRate(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    @FXML
    public void onClickHambarger(){
        transition.setRate(transition.getRate()*-1);
        transition.play();
        if(navDrawer.isClosed())
            navDrawer.open();
        else
            navDrawer.close();
    }

    
    
}
