package com.openclassrooms.mddapi.exceptions;


public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException(String message) {
        super(message);
    }
}
