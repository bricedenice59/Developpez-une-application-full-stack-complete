package com.openclassrooms.mddapi.payloads.responses;

import lombok.Data;

@Data
public class SimpleOutputMessageResponse {

    private String Message;
    public SimpleOutputMessageResponse(String message) {
        Message = message;
    }
}
