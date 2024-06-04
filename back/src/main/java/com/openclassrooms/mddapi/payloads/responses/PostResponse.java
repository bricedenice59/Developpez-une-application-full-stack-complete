package com.openclassrooms.mddapi.payloads.responses;

import lombok.*;


import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResponse {
    private Integer id;
    private Integer themeId;
    private String title;
    private String description;
    private String createdAt;
    private String author;
    private List<CommentResponse> comments;
}
