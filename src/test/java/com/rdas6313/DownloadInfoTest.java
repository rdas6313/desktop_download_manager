package com.rdas6313;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class DownloadInfoTest {
    
    @Test
    public void isInfoCreationHappening(){
        DownloadInfo info = new DownloadInfo("http://", "abc.txt", "file://",1, 20000);
        Assertions.assertEquals(1, info.getId());
    }
}
