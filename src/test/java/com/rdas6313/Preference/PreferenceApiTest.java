package com.rdas6313.Preference;

import java.util.prefs.Preferences;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PreferenceApiTest {
    private final String PATH = "/SETTINGS";

    private PreferenceHandler pHandler = new PreferenceApi(
        Preferences.userRoot().node(PATH)
    );

    @Test
    void testStore() {
        String KEY = "BOOLEAN_KEY";
        pHandler.store(KEY, true);
        Assertions.assertTrue(pHandler.retrieveBoolean(KEY));
    }

    @Test
    void testStore2() {
        String KEY = "INT_KEY";
        pHandler.store(KEY, 100);
        Assertions.assertEquals(100, pHandler.retrieveInt(KEY));
    }

    @Test
    void testStore3() {
        String KEY = "STRING_KEY";
        pHandler.store(KEY, "YO");
        Assertions.assertEquals("YO", pHandler.retrieveString(KEY));
    }
}
