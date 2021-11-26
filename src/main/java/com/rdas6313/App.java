package com.rdas6313;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


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
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                manager.onAppClose();
                Platform.exit();
                System.exit(0);
            }
            
        });
        stage.show();
    
    }

    public static void main(String[] args) {
        launch();
    }

   

    
}