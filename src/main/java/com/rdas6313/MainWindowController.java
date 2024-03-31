package com.rdas6313;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class MainWindowController extends Controller implements Initializable{


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

    private Timeline timeline;

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
        timeline = new Timeline();
        onClickHambarger();
    }

    
    @FXML
    private void onClickHambarger(){
        transition.setRate(transition.getRate()*-1);
        transition.play();
        if(navDrawer.isClosed()){
            navDrawer.open();
            timeline.stop();
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(getDrawerOpenAnimation(transition.getTotalDuration(),transition.getInterpolator()));
            timeline.play();
        }
        else{
            navDrawer.close();
            timeline.stop();
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(getDrawerCloseAnimation(transition.getTotalDuration(),transition.getInterpolator()));
            timeline.play();
        }
    }
    
    
    private KeyFrame getDrawerCloseAnimation(Duration duration, Interpolator interpolator){
        KeyFrame keyFrame = new KeyFrame(duration,
            new KeyValue(paneWindow.prefWidthProperty(),Config.APP_WINDOW_WIDTH,interpolator));
        return keyFrame;
    }

    private KeyFrame getDrawerOpenAnimation(Duration duration, Interpolator interpolator) {
        KeyFrame keyFrame = new KeyFrame(duration,
            new KeyValue(paneWindow.prefWidthProperty(),Config.INNER_WINDOW_WIDTH,interpolator));
        return keyFrame;
    }


    @Override
    protected String getFxmlPath() {
        return Config.LOAD_MAIN_WINDOW;
    }

    public void setView(Parent view){
        paneWindow.getChildren().clear();
        paneWindow.getChildren().add(view);
        AnchorPane.setTopAnchor(view, 0.0);
        AnchorPane.setBottomAnchor(view, 0.0);
        AnchorPane.setLeftAnchor(view, 0.0);
        AnchorPane.setRightAnchor(view, 0.0);      
       
    }

    public void setWindowTitle(String title){
        if(windowTitle == null)
            return;
        windowTitle.setText(title);
    }
    
}
