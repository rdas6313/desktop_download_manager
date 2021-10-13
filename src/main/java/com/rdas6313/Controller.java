package com.rdas6313;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public abstract class Controller extends Observable{
    
    private Parent rootView;
    private String fxmlResourcePath;

    private void loadFxml(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlResourcePath));
        fxmlLoader.setController(this);
        try {
            rootView = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Parent getRootView(){
        if(rootView == null){
            fxmlResourcePath = getFxmlPath();
            loadFxml();
        }
        return rootView;
    }

    protected abstract String getFxmlPath();

}
