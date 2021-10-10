package com.rdas6313;

import java.io.IOException;
import java.lang.management.ThreadInfo;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application implements ClickNotifiable,DownloadEvent{

    private ClickNotifiable clickNotifiable;
    private Loadable loadableMainWindow;
    private Loadable loadDrawerWindow;
    private MainWindowController controller;
    private Parent sidebarViews[];

    private Loadable loadNavBtnWindows[] = {
        new AddDownloadController(this),
        new RunningDownloadController()
    };

    @Override
    public void start(Stage stage) {
        try {
            sidebarViews = new Parent[5];
           
            int i = 0;
            for (Loadable loadable : loadNavBtnWindows) {
                sidebarViews[i++] = loadable.loadFxml();
            }
            
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

    @Override
    public void onAdd(DownloadInfo info) {
        DownloadEvent event = (DownloadEvent) loadNavBtnWindows[1];
        event.onAdd(info);        
    }

    
}