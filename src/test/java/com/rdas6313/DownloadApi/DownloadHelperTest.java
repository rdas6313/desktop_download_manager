package com.rdas6313.DownloadApi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DownloadHelperTest {
    @Test
    void testConnect() {
        String url = "https://unsplash.com/photos/46yjc9dA8MM/download?ixid=MnwxMjA3fDB8MXxhbGx8M3x8fHx8fDJ8fDE2Mzc4MjU0MTY&force=true";
        String req_method = "HEAD";
        Thread thread = new Thread(()->{
            HttpURLConnection conn = null;
            try {
                conn = DownloadHelper.connect(url, req_method);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Assertions.assertNotNull(conn);
        });
        thread.start();
    }
}
