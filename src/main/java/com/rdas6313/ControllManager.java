package com.rdas6313;

import com.rdas6313.DataBase.CompletedListHandler;
import com.rdas6313.DataBase.DbConnector;
import com.rdas6313.DataBase.DbHandler;
import com.rdas6313.DataBase.PausedListHandler;
import com.rdas6313.DataBase.SqlliteConnector;

import javafx.scene.Parent;

public class ControllManager extends Observable{

    private DrawerController drawerController;
    
    private MainWindowController mController;

    private DbConnector dbConnector = new SqlliteConnector();

    private DbHandler dbHandlers[] = {
        new PausedListHandler(dbConnector),
        new CompletedListHandler(dbConnector)
    };

    private TitleController tControllers[] = {
        new AddDownloadController(),
        new RunningDownloadController(dbHandlers),
        new PausedDownloadController(dbHandlers[0]),
        new CompletedDownloadController(dbHandlers[1])
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
        ((PausedListHandler)dbHandlers[0]).attach((PausedDownloadController)tControllers[2]);
        PausedDownloadController pausedDownloadController = (PausedDownloadController) tControllers[2];
        pausedDownloadController.attach((event)->{
            onAddDownload(event.getNewValue());
        });

        ((CompletedListHandler)dbHandlers[1]).attach((CompletedDownloadController)tControllers[3]);
    
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
            case Config.COMPLETED_DOWNLOAD_BUTTON_ID:
                value = 3;
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
