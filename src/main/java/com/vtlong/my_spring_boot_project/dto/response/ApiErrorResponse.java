package com.vtlong.my_spring_boot_project.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ApiErrorResponse<T> extends ApiResponse<T> {

    private String errorCode;
    private List<String> details;

    public static <T> ApiErrorResponse<T> error(int status, String message, String errorCode, List<String> details) {
        return ApiErrorResponse.<T>builder()
                .status(status)
                .message(message)
                .errorCode(errorCode)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiErrorResponse<T> error(int status, String message) {
        return ApiErrorResponse.<T>builder()
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiErrorResponse<T> error(int status, String message, String errorCode, List<String> details,
            String path, String requestId) {
        return ApiErrorResponse.<T>builder()
                .status(status)
                .message(message)
                .errorCode(errorCode)
                .details(details)
                .timestamp(LocalDateTime.now())
                .path(path)
                .requestId(requestId)
                .build();
    }

    public static <T> ApiErrorResponse<T> error(int status, String message, String errorCode, String path,
            String requestId) {
        return ApiErrorResponse.<T>builder()
                .status(status)
                .message(message)
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .path(path)
                .requestId(requestId)
                .build();
    }
}
