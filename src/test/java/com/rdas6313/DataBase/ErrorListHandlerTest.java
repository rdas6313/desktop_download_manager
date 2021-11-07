package com.rdas6313.DataBase;

import java.util.List;

import com.rdas6313.DownloadInfo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ErrorListHandlerTest {

    private DbHandler handler = new ErrorListHandler(new SqlliteConnector());

    @Test
    void testDelete() {
        int key_id = 1;
        handler.delete(key_id);
        List<DownloadInfo> list = handler.getList();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void testGetList() {
        List<DownloadInfo> list = handler.getList();
        for(DownloadInfo data: list){
            System.out.println(data.getFilename()+" "+data.getSize()+" "+data.getCurrentSize());
        }
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void testInsert() {
        DownloadInfo data = new DownloadInfo("http://", "tum hi ho.mp3", 
            "file://", 2, 200000, 25000);
        handler.insert(data);
        List<DownloadInfo> list = handler.getList();
        Assertions.assertEquals(1, list.size());
    }
}
