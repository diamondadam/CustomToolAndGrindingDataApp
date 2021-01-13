package com.customtoolandgrinding.customtooldataapp.source;

import java.io.IOException;

public class ConnectionError extends IOException {
    public ConnectionError(String errorMessage) {
        super(errorMessage);

    }
}
