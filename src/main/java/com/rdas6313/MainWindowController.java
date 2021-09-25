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
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MainWindowController implements Initializable,Loadable{


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

    public MainWindowController(Parent parent) {
        sideDrawer = (AnchorPane)parent;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        navDrawer.setSidePane(sideDrawer);
        sideDrawer.prefWidthProperty().bind(navDrawer.widthProperty());
        sideDrawer.prefHeightProperty().bind(navDrawer.heightProperty());
        transition = new HamburgerBackArrowBasicTransition(hambargerBtn);
        transition.setRate(-1);
       
    }

    @FXML
    private void onClickHambarger(){
        transition.setRate(transition.getRate()*-1);
        transition.play();
        if(navDrawer.isClosed())
            navDrawer.open();
        else
            navDrawer.close();
    }

    @Override
    public Parent loadFxml() throws IOException{
       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Config.LOAD_MAIN_WINDOW));
       fxmlLoader.setController(this);
       return fxmlLoader.load();
    }

    
    
}
