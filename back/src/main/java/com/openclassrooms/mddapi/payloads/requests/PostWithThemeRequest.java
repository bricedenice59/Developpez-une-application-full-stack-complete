package com.openclassrooms.mddapi.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostWithThemeRequest {

    @NotBlank(message = "A title is required!")
    private String title;

    @NotBlank(message = "A description is required!")
    private String description;
}
