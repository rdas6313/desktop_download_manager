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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MainWindowController implements Initializable,Loadable,ClickNotifiable{


    @FXML
    private JFXHamburger hambargerBtn;

    @FXML
    private Label windowTitle;

    @FXML
    private JFXDrawer navDrawer;

    @FXML
    private AnchorPane paneWindow;

    @FXML
    private Label testLabel;
    
    private HamburgerBackArrowBasicTransition transition;

    private AnchorPane sideDrawer;  

    private Node views[];

    public MainWindowController(Parent parent, Parent[] sidebarViews) {
        sideDrawer = (AnchorPane)parent;
        views = sidebarViews;
    }


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        navDrawer.setSidePane(sideDrawer);
        sideDrawer.prefWidthProperty().bind(navDrawer.widthProperty());
        sideDrawer.prefHeightProperty().bind(navDrawer.heightProperty());
        transition = new HamburgerBackArrowBasicTransition(hambargerBtn);
        transition.setRate(-1);
        changeWindowTitle(-1);

        
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

    @Override
    public void onbuttonClick(int buttonId) {
        
        paneWindow.getChildren().clear();
        paneWindow.getChildren().add(views[buttonId]);
        AnchorPane.setTopAnchor(views[buttonId], 0.0);
        AnchorPane.setBottomAnchor(views[buttonId], 0.0);
        AnchorPane.setLeftAnchor(views[buttonId], 0.0);
        AnchorPane.setRightAnchor(views[buttonId], 0.0);      
        changeWindowTitle(buttonId);
    }

    private void changeWindowTitle(int buttonId){
        switch(buttonId){
            case Config.ADD_DOWNLOAD_BUTTON_ID:
                windowTitle.setText(Config.ADD_DOWNLOAD_WINDOW_TITLE);
                break;
            case Config.RUNNING_DOWNLOAD_BUTTON_ID:
                windowTitle.setText(Config.RUNNING_DOWNLOAD_WINDOW_TITLE);
                break;
            default:
                windowTitle.setText(Config.WELCOME_MSG);
        }
    }
    
}
