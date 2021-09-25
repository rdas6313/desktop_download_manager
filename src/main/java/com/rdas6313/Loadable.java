package com.rdas6313;

import java.io.IOException;

import javafx.scene.Parent;

public interface Loadable {
    Parent loadFxml() throws IOException;
}
