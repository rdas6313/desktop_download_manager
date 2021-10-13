package com.rdas6313;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application{

 

    @Override
    public void start(Stage stage) {
        
        ControllManager manager = new ControllManager();
        Scene scene = new Scene(manager.getView(),Config.APP_WINDOW_WIDTH,Config.APP_WINDOW_HEIGHT);
        stage.setTitle(Config.APP_TITLE);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    
    }

    public static void main(String[] args) {
        launch();
    }

   

    
}