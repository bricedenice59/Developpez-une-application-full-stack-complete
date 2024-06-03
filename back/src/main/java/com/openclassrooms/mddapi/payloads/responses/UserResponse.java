package com.openclassrooms.mddapi.payloads.responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponse {
    private String name;
    private String email;
}
