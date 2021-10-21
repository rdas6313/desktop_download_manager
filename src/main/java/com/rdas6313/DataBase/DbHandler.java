package com.rdas6313.DataBase;

import java.util.List;

import com.rdas6313.DownloadInfo;

public interface DbHandler {
    void insert(DownloadInfo data);
    void delete(int key_id);
    List<DownloadInfo> getList();
}
