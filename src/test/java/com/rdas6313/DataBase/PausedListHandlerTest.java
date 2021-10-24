package com.rdas6313.DataBase;

import java.util.List;

import com.rdas6313.DownloadInfo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PausedListHandlerTest {
    
    private DbHandler dbHandler = new PausedListHandler(new SqlliteConnector());
        

    @Test
    void testDelete() {
        int key = 5;
        int currentSize = getDownloadListSize();
        dbHandler.delete(key);
        Assertions.assertEquals(currentSize-1, getDownloadListSize());
    }

    @Test
    void testGetList() {
        List<DownloadInfo> list = dbHandler.getList();
        for(DownloadInfo info: list){
            System.out.println(info.getId() +" "+ info.getFilename());
        }
        Assertions.assertNotNull(list);
    }

    @Test
    void testInsert() {
        DownloadInfo data = new DownloadInfo("http://", "Muskorane ki wajaa.mp3", "file://", 2, 200000,2331);
        int currentSize = getDownloadListSize();
        dbHandler.insert(data);
        Assertions.assertEquals(currentSize+1, getDownloadListSize());
    }

    private int getDownloadListSize(){
        List<DownloadInfo> list = dbHandler.getList();
        return list.size();
    }
}
