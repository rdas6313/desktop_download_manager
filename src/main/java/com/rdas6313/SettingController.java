package com.rdas6313;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.rdas6313.Preference.PrefConfig;
import com.rdas6313.Preference.PreferenceHandler;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class SettingController extends TitleController implements Initializable,EventHandler<ActionEvent>{

    @FXML
    private JFXButton saveBtn;

    @FXML
    private Spinner<Integer> threadlimit;

    @FXML
    private TextField uname;

    @FXML
    private Label notificationLabel;


    @FXML
    private Label errorLabel;
    
    private PreferenceHandler preferenceHandler;


    public SettingController(PreferenceHandler preferenceHandler) {
        this.preferenceHandler = preferenceHandler;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveBtn.setOnAction(this);
        if(preferenceHandler == null){
            System.err.println(getClass().getSimpleName()+" initialize: Preference handler is null.");
            return;
        }
        uname.setText(preferenceHandler.retrieveString(PrefConfig.CURRENT_USER));
        threadlimit.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
            Config.MIN_THREAD_LIMIT, Config.MAX_THREAD_LIMIT,preferenceHandler.retrieveInt(PrefConfig.THREAD_LIMIT
        )));
    }

    
    
    @Override
    public void handle(ActionEvent event) {
        int threadLimit = threadlimit.getValue();
        String user_name = uname.getText();
    //    System.out.println(getClass().getSimpleName()+" thread limit:"+threadLimit+" \nuname: "+user_name);
        errorLabel.setVisible(false);
        notificationLabel.setVisible(false);
        if(user_name == null || user_name.isEmpty()){
            errorLabel.setVisible(true);
            errorLabel.setText(Config.USER_NAME_EMPTY_ERROR_MSG);
            return;
        }
        if(preferenceHandler == null){
            errorLabel.setVisible(true);
            errorLabel.setText(Config.SOMETHING_WRONG_ERROR_MSG);
            System.err.println(getClass().getSimpleName()+" handle: Preference handler is null.");
            return;
        }
        
        preferenceHandler.store(PrefConfig.CURRENT_USER, user_name);
        preferenceHandler.store(PrefConfig.THREAD_LIMIT, threadLimit);
        notificationLabel.setVisible(true);
        notificationLabel.setText(Config.SUCCESS_NOTIFICATION);
    }
    
    private void onStart() {
        notificationLabel.setVisible(false);
        errorLabel.setVisible(false);
    }

    @Override
    public String getWindowTitle() {
        onStart();
        return Config.SETTINGS_WINDOW_TITLE;
    }


    @Override
    protected String getFxmlPath() {
        return Config.LOAD_SETTINGS_WINDOW;
    }
}
