package com.rdas6313.DownloadApi;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;

public class DownloadTask {

    private DownloadResponse res;

    public DownloadTask(DownloadResponse res) {
        this.res = res;
    }

    public void setDownloadResponse(DownloadResponse res){
        this.res = res;
    }

    public void getHead(D_file dataFile) {
        HashMap<String,String> data = new HashMap<>();
        HttpURLConnection conn = null;
        String url_address = dataFile.getUrl();
        
        int d_id = dataFile.getId();
        String req_method = DownloadApiConfig.HEAD_REQUEST;
        String redirect_header = DownloadApiConfig.REDIRECT_HEADER;
        String redirection_regex = DownloadApiConfig.REDIRECTION_REGEX;
        boolean shouldRedirect;
        
        do {
            shouldRedirect = false;
            try {
                conn = DownloadHelper.connect(url_address, req_method);
                conn = DownloadHelper.checkResponseCode(conn);
                conn = DownloadHelper.putHeadersInData(conn, data);
                String file_name = DownloadHelper.calculateName(conn);
                DownloadHelper.putFilenameInHeader(data, file_name);
                res.onHeaderInfo(data);
            } catch (NullPointerException e) {
                System.err.println(e.getMessage());
                res.onError(d_id,DownloadApiConfig.NULL_EXCEPTION_CODE,e.getMessage());
            } catch (MalformedURLException e) {
                System.err.println(e.getMessage());
                res.onError(d_id,DownloadApiConfig.URL_EXCEPTION_CODE,e.getMessage());
            } catch (IOException e) {
                System.err.println(e.getMessage());
                res.onError(d_id,DownloadApiConfig.INPUT_OUTPUT_EXCEPTION_CODE,e.getMessage());
            } catch (RuntimeException e) {
                if (e.getMessage().matches(redirection_regex)) {
                    shouldRedirect = true;
                    url_address = conn.getHeaderField(redirect_header);
                } else {
                    System.err.println(e.getMessage());
                    res.onError(d_id,DownloadApiConfig.RUNTIME_EXCEPTION_CODE,e.getMessage());
                }
            } finally {
                if (conn == null)
                    return;
                conn.disconnect();
                conn = null;
            }
        } while (shouldRedirect);
        dataFile.setUrl(url_address);
    }    
}
