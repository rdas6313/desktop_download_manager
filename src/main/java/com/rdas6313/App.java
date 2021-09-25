package com.rdas6313;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(Config.LOAD_MAIN_WINDOW));
            Scene scene = new Scene(root,Config.APP_WINDOW_WIDTH,Config.APP_WINDOW_HEIGHT);
            stage.setTitle(Config.APP_TITLE);
           
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}