package com.openclassrooms.mddapi.models.responses;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ApiErrorResponse {

    private Integer errorCode;
    private String errorMessage;
    private String stackTrace;
    private LocalDateTime timestamp;

    public ApiErrorResponse(Integer errorCode, Set<String> errorMessages, LocalDateTime timestamp) {
        this.errorCode = errorCode;
        this.errorMessage = StringUtils.join(errorMessages, ",");
        this.timestamp = timestamp;
    }

    public ApiErrorResponse(Integer errorCode, String errorMessage, LocalDateTime timestamp) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = timestamp;
    }
}
