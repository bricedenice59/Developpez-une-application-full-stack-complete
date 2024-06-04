package com.openclassrooms.mddapi.exceptions;


public class ThemeNotFoundException extends RuntimeException {
    public ThemeNotFoundException(String message) {
        super(message);
    }
}
