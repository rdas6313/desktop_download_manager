package com.rdas6313;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application implements ClickNotifiable{

    private ClickNotifiable clickNotifiable;
    private Loadable loadableMainWindow;
    private Loadable loadDrawerWindow;
    private Loadable loadAddDownloadWindow;
    private MainWindowController controller;
    private Parent sidebarViews[];

    @Override
    public void start(Stage stage) {
        try {
            sidebarViews = new Parent[5];

            loadAddDownloadWindow = new AddDownloadController();
            sidebarViews[0] = loadAddDownloadWindow.loadFxml();

            
            
            loadDrawerWindow = new DrawerController(this);
            Parent sidepane = loadDrawerWindow.loadFxml();
           
            controller = new MainWindowController(sidepane,sidebarViews);
            loadableMainWindow = controller;
            clickNotifiable = controller;
            Parent root = loadableMainWindow.loadFxml();

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

    @Override
    public void onbuttonClick(int buttonId) {
        clickNotifiable.onbuttonClick(buttonId);
    }

    
}