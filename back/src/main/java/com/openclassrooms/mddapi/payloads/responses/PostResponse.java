package com.openclassrooms.mddapi.payloads.responses;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResponse {
    private Integer id;
    private Integer topicId;
    private String topicName;
    private String title;
    private String description;
    private String createdAt;
    private String author;
}
