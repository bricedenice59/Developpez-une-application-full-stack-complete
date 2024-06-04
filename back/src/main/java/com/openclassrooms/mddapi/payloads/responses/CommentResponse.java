package com.openclassrooms.mddapi.payloads.responses;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CommentResponse {
    private Integer id;
    private String username;
    private String text;
    private String createdAt;
}
