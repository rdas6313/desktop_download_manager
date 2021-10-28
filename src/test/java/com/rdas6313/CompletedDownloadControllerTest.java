package com.rdas6313;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompletedDownloadControllerTest {
    @Test
    void testOpenFolder() {
        CompletedDownloadController controller = new CompletedDownloadController(null);
        
        Assertions.assertThrows(Exception.class, ()->{
            controller.openFolder("/home/rdas6313/Documents/Sem Project/desktop_download_manager");
        });
    }
}
