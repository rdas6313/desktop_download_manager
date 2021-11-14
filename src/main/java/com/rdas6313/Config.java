package com.rdas6313;

public class Config {
    //Setting titles here
    public static final String SETTINGS_WINDOW_TITLE = "Settings";
    public static final String APP_TITLE = "Download Manager";
    public static final String ADD_DOWNLOAD_WINDOW_TITLE = "Add Download";
    public static final String RUNNING_DOWNLOAD_WINDOW_TITLE = "Running Downloads";
    public static final String WELCOME_MSG = "Welcome";
    public static final String PAUSED_DOWNLOAD_TITLE = "Paused Downloads";
    public static final String COMPLETED_DOWNLOAD_WINDOW_TITLE = "Completed Downloads";
    public static final String ERROR_DOWNLOAD_TITLE = "Error Downloads";
    //Setting Scene window here
    public static final int APP_WINDOW_WIDTH = 700;
    public static final int APP_WINDOW_HEIGHT = 450;
    public static final int INNER_WINDOW_WIDTH = 655;
    //setting fxml path here
    public static final String LOAD_SETTINGS_WINDOW = "/fxml/settings.fxml";
    public static final String LOAD_MAIN_WINDOW = "/fxml/MainWindow.fxml";
    public static final String LOAD_NAV_DRAWER = "/fxml/navdrawer.fxml";
    public static final String LOAD_ADD_DOWNLOAD_WINDOW = "/fxml/addDownload.fxml";
    public static final String LOAD_RUNNING_DOWNLOAD = "/fxml/runningDownload.fxml";
    public static final String LOAD_RUNNING_LIST_ITEM = "/fxml/runningListItem.fxml";
    public static final String LOAD_PAUSED_DOWNLOAD = "/fxml/pausedDownload.fxml";
    public static final String LOAD_PAUSED_LIST_ITEM = "/fxml/pausedListItem.fxml";
    public static final String LOAD_COMPLETED_DOWNLOAD = "/fxml/completedDownload.fxml";
    public static final String LOAD_COMPLETED_LIST_ITEM = "/fxml/completedListItem.fxml";
    public static final String LOAD_ERROR_DOWNLOAD = "/fxml/errorDownload.fxml";
    public static final String LOAD_ERROR_LIST_ITEM = "/fxml/errorListItem.fxml";
    
    //setting drawer buttons id
    public static final String ADD_DOWNLOAD_BUTTON_ID = "addDownloadBtn";
    public static final String RUNNING_DOWNLOAD_BUTTON_ID = "ongoingDownloadBtn";
    public static final String PAUSED_DOWNLOAD_BUTTON_ID = "pausedDownloadBtn";
    public static final String ERROR_DOWNLOAD_BUTTON_ID = "errorDownloadBtn";
    public static final String COMPLETED_DOWNLOAD_BUTTON_ID = "completedDownloadBtn";
    public static final String SETTINGS_BUTTON_ID = "settingsBtn";
    //setting error messages
    public static final String URL_ERROR_MSG = "Please enter a valid url";
    public static final String EMPTY_FILE_PATH_MSG = "Please choose a save location";
    public static final String GET_INFO_MSG = "Please get download information first";
    public static final String FILE_NAME_ERROR = "Please Enter a file name";
    public static final String DOWNLOAD_ADDED_BODY_MSG = "Download added successfully";
    public static final String DOWNLOAD_ADDED_HEADER_MSG = "SUCCESS";
    public static final String USER_NAME_EMPTY_ERROR_MSG = "User name field can't be empty.";
    public static final String SOMETHING_WRONG_ERROR_MSG = "Something went wrong.";
    //Setting button text
    public static final String DIALOG_BTN_TEXT = "Okay";
    //setting Exception messages
    public static final String OBSERVABLE_LIST_EXCEPTION_MSG = "Observable list is null";
    public static final String DOWNLOAD_INFO_EXCEPTION_MSG = "Download Info is null";
    //Notification messages
    public static final String ADD_DOWNLOAD_NOTIFICATION = "ADD_DOWNLOAD";
    public static final String ITEM_ADDED_RUNNING_DOWNLOAD_NOTIFICATION = "item_add_to_running_download";
    public static final String SUCCESS_NOTIFICATION = "Successfully Saved";
    //no of threads limit
    public static final int MIN_THREAD_LIMIT = 1;
    public static final int MAX_THREAD_LIMIT = 5;
    
   
    
    
    
   
   
    
    
    
    
    
    
    
    

}
