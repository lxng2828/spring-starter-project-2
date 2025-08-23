package com.vtlong.my_spring_boot_project.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ApiResponse<T> {

    private Integer status;
    private String message;
    private T data;
    private String path;

}
