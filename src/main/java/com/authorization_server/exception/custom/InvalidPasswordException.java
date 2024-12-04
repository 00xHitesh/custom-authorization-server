package com.authorization_server.exception.custom;

import java.io.Serial;

public class InvalidPasswordException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidPasswordException(String message) {
        super(message);
    }
}