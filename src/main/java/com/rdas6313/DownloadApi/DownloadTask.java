package com.rdas6313.DownloadApi;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DownloadTask {

    private DownloadResponse res;

    public DownloadTask(DownloadResponse res) {
        this.res = res;
    }

    public void setDownloadResponse(DownloadResponse res){
        this.res = res;
    }

    public boolean headerDownload(D_file dataFile) {
        Map<String,String> headerData = new HashMap<>();
        HttpURLConnection conn = null;
        String url_address = dataFile.getUrl();
        int d_id = dataFile.getId();
        String req_method = DownloadApiConfig.HEAD_REQUEST;
        String redirect_header = DownloadApiConfig.REDIRECT_HEADER;
        String redirection_regex = DownloadApiConfig.REDIRECTION_REGEX;
        boolean shouldRedirect;
        int redirection_count = -1;
        boolean isThereException = false;
        do {
            shouldRedirect = false;
            try {
                if(DownloadHelper.isHttpAndHttps(url_address)){
                    res.onError(d_id,DownloadApiConfig.URL_EXCEPTION_CODE,"Only http and https protocol is allowed");
                    return false;
                }
                redirection_count++;
                conn = DownloadHelper.connect(url_address, req_method);
                conn = DownloadHelper.checkResponseCode(conn);
                conn = DownloadHelper.putHeadersInData(conn, headerData);
                String file_name = DownloadHelper.calculateName(conn);
                DownloadHelper.putFilenameInHeader(headerData, file_name);
                if(!dataFile.isCancelled() && res != null)
                    res.onHeaderInfo(headerData);
            } catch (NullPointerException e) {
                isThereException = true;
                System.err.println(e.getMessage());
                if(res != null)
                    res.onError(d_id,DownloadApiConfig.NULL_EXCEPTION_CODE,e.getMessage());
            } catch (MalformedURLException e) {
                isThereException = true;
                System.err.println("Malformed Url - " + e.getMessage());
                if(res != null)
                    res.onError(d_id,DownloadApiConfig.URL_EXCEPTION_CODE,e.getMessage());
            } catch (IOException e) {
                isThereException = true;
                System.err.println(e.getMessage());
                if(res != null)
                    res.onError(d_id,DownloadApiConfig.INPUT_OUTPUT_EXCEPTION_CODE,e.getMessage());
            } catch (RuntimeException e) {
                if (e.getMessage().matches(redirection_regex)) {
                    shouldRedirect = true;
                    url_address = conn.getHeaderField(redirect_header);
                } else {
                    isThereException = true;
                    System.err.println("Runtime Error: " + e.getMessage());
                    if(res != null)
                        res.onError(d_id,DownloadApiConfig.RUNTIME_EXCEPTION_CODE,"Oops! Unknown error occoured");
                }
            } finally {
                if (conn == null)
                    return isThereException;
                conn.disconnect();
                conn = null;
            }
        } while (shouldRedirect);
        dataFile.setUrl(url_address);
        String file_size = headerData.get(DownloadApiConfig.FILE_SIZE);
        if(file_size != null && !file_size.isEmpty()) dataFile.setFileSize(Long.parseLong(file_size));
        else dataFile.setFileSize(0);
        if(redirection_count > 0) {
            System.out.println("Total redirection : " + redirection_count);
            System.out.println("Final redirected url : "+url_address);
        }
        return isThereException;
    }
    
    public void fileDownload(D_file dataFile){
        String file_name = dataFile.getFileName();
        String req_method = "GET";
        String fileSize_header = "file-size";
        int BYTE_ARRAY_SIZE = 1024*256;
        long remote_filesize = 0;
        String savePath = dataFile.getStorage();
        String url = dataFile.getUrl();
        HttpURLConnection conn = null;
        InputStream in = null;
        FileOutputStream out = null;
        try {
            DownloadHelper.isDirExist(savePath);
            long local_file_size = DownloadHelper.getLocalFileSize(file_name, savePath);

            boolean isThereException = headerDownload(dataFile);
            if(isThereException) return;
            url = dataFile.getUrl();
            remote_filesize = dataFile.getFileSize();

            res.onStart(dataFile.getId(), dataFile.getFileName(), dataFile.getStorage(), dataFile.getUrl(), local_file_size, remote_filesize);
            //sleep(2);
            conn = DownloadHelper.connect(url, req_method);
            //System.out.println(getClass().getSimpleName()+": local size: "+local_file_size+" , remote size: "+remote_filesize);
            conn = DownloadHelper.setHeaderRange(local_file_size, remote_filesize, conn);
            conn = DownloadHelper.checkResponseCode(conn);
            in = conn.getInputStream();
            byte temp[] = new byte[BYTE_ARRAY_SIZE];
            int bytesRead = 0;
            out = new FileOutputStream(savePath + "/" + file_name, true);
            long percentage = 0;
            long downloaded_datasize = local_file_size;
            
            while ((bytesRead = in.read(temp)) != -1 && !dataFile.isCancelled()) {
                out.write(temp, 0, bytesRead);
                downloaded_datasize = downloaded_datasize + bytesRead;
                res.onProgress(dataFile.getId(), dataFile.getFileName(), dataFile.getStorage(), dataFile.getUrl(), downloaded_datasize, remote_filesize);
               // sleep(2);
            }
            if(dataFile.isCancelled())
                res.onStop(dataFile.getId(), dataFile.getFileName(), dataFile.getStorage(), dataFile.getUrl(), downloaded_datasize, remote_filesize);
            else
                res.onComplete(dataFile.getId(), dataFile.getFileName(), dataFile.getStorage(), dataFile.getUrl(), remote_filesize, remote_filesize);
            
        }catch(ProtocolException e){
            res.onError(dataFile.getId(), DownloadApiConfig.PROTOCOL_EXCEPTION_CODE, e.getMessage());
            System.err.println(e.getMessage());
        }catch(FileNotFoundException e){
            System.err.println(e.getMessage());
            res.onError(dataFile.getId(), DownloadApiConfig.FILE_NOT_FOUND_EXCEPTION_CODE, e.getMessage());
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
            res.onError(dataFile.getId(), DownloadApiConfig.ARUGUMENT_EXCEPTION_CODE, e.getMessage());
        } catch (NullPointerException e) {
            //e.printStackTrace();
            System.err.println(e.getMessage());
            res.onError(dataFile.getId(), DownloadApiConfig.NULL_EXCEPTION_CODE, e.getMessage());
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
            res.onError(dataFile.getId(), DownloadApiConfig.URL_EXCEPTION_CODE, e.getMessage());
            //e.printStackTrace();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            res.onError(dataFile.getId(), DownloadApiConfig.INPUT_OUTPUT_EXCEPTION_CODE, e.getMessage());
            //e.printStackTrace();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            res.onError(dataFile.getId(), DownloadApiConfig.RUNTIME_EXCEPTION_CODE, e.getMessage());
        }finally {
            try{
                in.close();
                out.close();
            }catch(NullPointerException e){
                res.onError(dataFile.getId(), DownloadApiConfig.NULL_EXCEPTION_CODE, e.getMessage());
                System.err.println(e.getMessage());
            }catch(IOException e){
                System.err.println(e.getMessage());
                res.onError(dataFile.getId(), DownloadApiConfig.NULL_EXCEPTION_CODE, e.getMessage());
            }
            if (conn == null)
                return;
            conn.disconnect();
            conn = null;
        }

    }

    private void sleep(int sec){
        try {
            Thread.currentThread().sleep(sec*1000);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
}
