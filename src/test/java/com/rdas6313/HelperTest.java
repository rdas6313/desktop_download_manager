package com.rdas6313;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HelperTest {
    @Test
    void testCreateDir() {
        String path = System.getProperty("user.home") + "/download_manager/";
        Assertions.assertTrue(Helper.createDir(path));
    }

    @Test
    void testCalculateSizeInText() {
        long b = 23115110;
        long a = 5068417;
        long s = 3210916;
        String size = Helper.calculateSizeInText(b);
        System.out.println(size);
    }
}
