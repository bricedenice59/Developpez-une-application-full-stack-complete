package com.openclassrooms.mddapi.payloads.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateUserDetailsRequest {
    @NotBlank(message = "Name is required!")
    private String name;

    @NotBlank(message = "Email is required!")
    private String email;
}
