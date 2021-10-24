package com.rdas6313;

import com.rdas6313.DataBase.PausedListHandler;
import com.rdas6313.DataBase.SqlliteConnector;

import javafx.scene.Parent;

public class ControllManager extends Observable{

    private DrawerController drawerController;
    
    private MainWindowController mController;

    private PausedListHandler pausedListHandler = new PausedListHandler(new SqlliteConnector());

    private TitleController tControllers[] = {
        new AddDownloadController(),
        new RunningDownloadController(pausedListHandler),
        new PausedDownloadController(pausedListHandler)
    };

    public ControllManager(){
        drawerController = new DrawerController();
        drawerController.attach((event)->{
            onClickNavDrawerBtn(event.getNewValue());
        });
        
        Parent drawerRootView = drawerController.getRootView();
        mController = new MainWindowController(drawerRootView);
        

        AddDownloadController addDownloadController = (AddDownloadController) tControllers[0];
        addDownloadController.attach((event)->{
            onAddDownload(event.getNewValue());
        });
        
        RunningDownloadController runningDownloadController = (RunningDownloadController) tControllers[1];
        attach(runningDownloadController);
        pausedListHandler.attach((PausedDownloadController)tControllers[2]);
        PausedDownloadController pausedDownloadController = (PausedDownloadController) tControllers[2];
        pausedDownloadController.attach((event)->{
            onAddDownload(event.getNewValue());
        });
    }

    private void onAddDownload(Object newValue) {
        notifyObservers(Config.ITEM_ADDED_RUNNING_DOWNLOAD_NOTIFICATION, null, newValue);        
    }

    private void onClickNavDrawerBtn(Object newValue) {
        String btnId = (String) newValue;
        int value = -1;
        
        switch(btnId){
            case Config.ADD_DOWNLOAD_BUTTON_ID:
                value = 0;        
                break;
            case Config.RUNNING_DOWNLOAD_BUTTON_ID:
                value = 1;
                break;
            case Config.PAUSED_DOWNLOAD_BUTTON_ID:
                value = 2;
                break;
            default:
                return;
        }
        System.out.println("NavDrawerBtn value "+value);
        mController.setView(tControllers[value].getRootView());
        mController.setWindowTitle(tControllers[value].getWindowTitle());
    }

    public Parent getView(){
        Parent root = mController.getRootView();
        mController.setWindowTitle(Config.WELCOME_MSG);
        return root;
    }
  
    
}
