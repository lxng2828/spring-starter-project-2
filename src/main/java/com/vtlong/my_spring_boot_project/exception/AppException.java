package com.vtlong.my_spring_boot_project.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final Integer status;

    public AppException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
