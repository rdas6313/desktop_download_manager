package com.rdas6313.DownloadApi;

import java.util.HashMap;
import java.util.Map;

public class RunningListManager {
    
    private Map<Integer,D_file> map = new HashMap<Integer,D_file>();
    private int index = 0;

    public int store(D_file data){
        map.put(index, data);
        return index++;
    }

    public D_file get(int id){
        return map.get(id);
    }

    public D_file remove(int id){
        return map.remove(id);
    }
}
