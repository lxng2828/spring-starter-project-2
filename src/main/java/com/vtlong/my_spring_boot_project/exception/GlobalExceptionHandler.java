package com.vtlong.my_spring_boot_project.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vtlong.my_spring_boot_project.dto.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(AppException.class)
        public ResponseEntity<ApiResponse<Void>> handleAppException(AppException ex, HttpServletRequest request) {
                String path = request.getRequestURI();

                logger.warn("AppException occurred: {} - {} | Code: {} | Path: {} | Method: {}",
                                ex.getClass().getSimpleName(),
                                ex.getMessage(),
                                ex.getStatus(),
                                path,
                                request.getMethod());

                ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                                .status(ex.getStatus())
                                .message(ex.getMessage())
                                .path(path)
                                .build();

                return ResponseEntity.status(ex.getStatus()).body(errorResponse);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException ex, HttpServletRequest request) {

                List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .collect(Collectors.toList());

                String path = request.getRequestURI();

                logger.warn("Validation error occurred: {} | Path: {} | Method: {} | Errors: {}",
                                ex.getClass().getSimpleName(),
                                path,
                                request.getMethod(),
                                errors);

                ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                                .status(Integer.valueOf(HttpStatus.BAD_REQUEST.value()))
                                .message("Dữ liệu đầu vào không hợp lệ")
                                .path(path)
                                .build();

                return ResponseEntity.badRequest().body(errorResponse);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(
                        ConstraintViolationException ex, HttpServletRequest request) {

                List<String> errors = ex.getConstraintViolations()
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.toList());

                String path = request.getRequestURI();

                logger.warn("Constraint violation occurred: {} | Path: {} | Method: {} | Errors: {}",
                                ex.getClass().getSimpleName(),
                                path,
                                request.getMethod(),
                                errors);

                ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                                .status(Integer.valueOf(HttpStatus.BAD_REQUEST.value()))
                                .message("Dữ liệu yêu cầu không hợp lệ")
                                .path(path)
                                .build();

                return ResponseEntity.badRequest().body(errorResponse);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex,
                        HttpServletRequest request) {

                String path = request.getRequestURI();

                logger.error("RuntimeException occurred: {} - {} | Path: {} | Method: {} | Stack trace: ",
                                ex.getClass().getSimpleName(),
                                ex.getMessage(),
                                path,
                                request.getMethod(),
                                ex);

                ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                                .status(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                .message("Lỗi runtime: " + ex.getMessage())
                                .path(path)
                                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex, HttpServletRequest request) {

                String path = request.getRequestURI();

                logger.error("Unexpected exception occurred: {} - {} | Path: {} | Method: {} | Stack trace: ",
                                ex.getClass().getSimpleName(),
                                ex.getMessage(),
                                path,
                                request.getMethod(),
                                ex);

                ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                                .status(Integer.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                                .message("Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.")
                                .path(path)
                                .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
}
