package com.rdas6313.DownloadApi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class DownloadHelperTest {

    @Test
    void testMakeDownloadDir() {
        
        Assertions.assertThrows(IllegalArgumentException.class, ()-> {
            DownloadHelper.isDirExist("null");
        },"Dir Exist"); 

        Assertions.assertThrows(IllegalArgumentException.class, ()-> {
            DownloadHelper.isDirExist("/home/rdas6313/Music/testFolder");
        },"Dir Exist");
    }

    @Test
    void testGetLocalFileSize() {
        String file_name = "Aashiqui Aa Gayi - Radhe Shyam 320 Kbps.mp3";
        String path = "weawsrar";

        String file_name1 = "Aashiqui Aa Gayi - Radhe Shyam 320 Kbps.mp3";
        String path1 = "/home/rdas6313/Music/testFolder";
        try {
            Assertions.assertEquals(0, DownloadHelper.getLocalFileSize(file_name1, path1));
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Assertions.assertThrows(IllegalArgumentException.class, ()-> {
            long size = DownloadHelper.getLocalFileSize(file_name, path);
            System.out.println("file size: "+size);
        },"Got local file size successfully");
    }

    @Test
    void testConnect2() {
        String url = "";// it should be like http://www.google.com
        String req_method = "HEAD";
        Assertions.assertThrows(MalformedURLException.class, ()->{
            HttpURLConnection conn = DownloadHelper.connect(url, req_method);
            System.out.println("Response code "+conn.getResponseCode());
        },"Connected Successfully");

        String url2 = "http://www.google.com";
        String req_method2 = "";        
        Assertions.assertThrows(ProtocolException.class, ()->{
            HttpURLConnection conn = DownloadHelper.connect(url2, req_method2);
            
        },"Connected Successfully");

    }

    @Test
    void testGetRemoteFileSize() {
        String url = "https://github.com/rdas6313/desktop_download_manager/archive/refs/heads/master.zip";
        String req_method = "GET";
        HttpURLConnection conn = null;
        try {
            conn = DownloadHelper.connect(url, req_method);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Assertions.assertEquals(0, DownloadHelper.getRemoteFileSize(conn));

    }
}
