package com.openclassrooms.mddapi.payloads.requests;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentPostRequest {

    @NotBlank(message = "A valid comment is required!")
    private String comment;
}

