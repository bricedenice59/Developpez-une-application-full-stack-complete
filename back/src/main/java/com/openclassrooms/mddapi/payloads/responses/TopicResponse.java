package com.openclassrooms.mddapi.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TopicResponse {
    private Integer id;
    private String title;
    private String description;
}
