package com.rdas6313;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelperTest {
    @Test
    void testCreateDir() {
        String path = System.getProperty("user.home") + "/download_manager/";
        Assertions.assertTrue(Helper.createDir(path));
    }
}
