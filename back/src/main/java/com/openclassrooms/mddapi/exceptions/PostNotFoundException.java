package com.openclassrooms.mddapi.exceptions;


public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
    }
}
