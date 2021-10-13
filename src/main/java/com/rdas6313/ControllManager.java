package com.rdas6313;

import javafx.scene.Parent;

public class ControllManager extends Observable{

    private DrawerController drawerController;
    
    private MainWindowController mController;

    private TitleController tControllers[] = {
        new AddDownloadController(),
        new RunningDownloadController()
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
    }

    private void onAddDownload(Object newValue) {
        notifyObservers("DOWNLOAD_ADD_TO_LIST", null, newValue);        
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
        }
        
        mController.setView(tControllers[value].getRootView());
        mController.setWindowTitle(tControllers[value].getWindowTitle());
    }

    public Parent getView(){
        Parent root = mController.getRootView();
        mController.setWindowTitle(Config.WELCOME_MSG);
        return root;
    }
  
    
}
