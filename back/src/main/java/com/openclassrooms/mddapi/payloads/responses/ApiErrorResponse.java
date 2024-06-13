package com.openclassrooms.mddapi.payloads.responses;

import com.openclassrooms.mddapi.utils.DateFormatter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ApiErrorResponse {

    private Integer errorCode;
    private String errorMessage;
    private String stackTrace;
    private String timestamp;

    public ApiErrorResponse(Integer errorCode, Set<String> errorMessages, LocalDateTime timestamp) {
        this.errorCode = errorCode;
        this.errorMessage = StringUtils.join(errorMessages, ",");
        this.timestamp = formatDate(timestamp);
    }

    public ApiErrorResponse(Integer errorCode, String errorMessage, LocalDateTime timestamp) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = formatDate(timestamp);
    }

    private String formatDate(LocalDateTime date) {
        return date.format(DateFormatter.getFormatter());
    }
}
