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

import com.vtlong.my_spring_boot_project.dto.response.ApiErrorResponse;
import com.vtlong.my_spring_boot_project.util.RequestContextUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        private HttpStatus determineHttpStatus(ErrorCode errorCode) {
                switch (errorCode) {
                        case USER_NOT_FOUND:
                        case ROLE_NOT_FOUND:
                        case RESOURCE_NOT_FOUND:
                                return HttpStatus.NOT_FOUND;
                        case USER_ALREADY_EXISTS:
                        case ROLE_ALREADY_EXISTS:
                                return HttpStatus.CONFLICT;
                        case USER_INVALID_INPUT:
                        case VALIDATION_ERROR:
                        case BAD_REQUEST:
                                return HttpStatus.BAD_REQUEST;
                        case USER_UNAUTHORIZED:
                                return HttpStatus.UNAUTHORIZED;
                        case METHOD_NOT_ALLOWED:
                                return HttpStatus.METHOD_NOT_ALLOWED;
                        case REQUEST_TIMEOUT:
                                return HttpStatus.REQUEST_TIMEOUT;
                        case TOO_MANY_REQUESTS:
                                return HttpStatus.TOO_MANY_REQUESTS;
                        case SERVICE_UNAVAILABLE:
                                return HttpStatus.SERVICE_UNAVAILABLE;
                        case INTERNAL_SERVER_ERROR:
                        default:
                                return HttpStatus.INTERNAL_SERVER_ERROR;
                }
        }

        @ExceptionHandler(AppException.class)
        public ResponseEntity<ApiErrorResponse<Void>> handleAppException(AppException ex, HttpServletRequest request) {
                ErrorCode errorCode = ex.getErrorCode();
                HttpStatus httpStatus = determineHttpStatus(errorCode);
                String requestId = RequestContextUtil.getRequestId();
                String path = request.getRequestURI();

                logger.warn("AppException occurred: {} - {} | Path: {} | Method: {} | ErrorCode: {} | RequestId: {}",
                                ex.getClass().getSimpleName(),
                                ex.getMessage(),
                                path,
                                request.getMethod(),
                                errorCode.getCode(),
                                requestId);

                ApiErrorResponse<Void> errorResponse = ApiErrorResponse.error(
                                httpStatus.value(),
                                ex.getMessage(),
                                errorCode.getCode(),
                                path,
                                requestId);

                return ResponseEntity.status(httpStatus).body(errorResponse);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse<Void>> handleMethodArgumentNotValidException(
                        MethodArgumentNotValidException ex, HttpServletRequest request) {

                List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .collect(Collectors.toList());

                String requestId = RequestContextUtil.getRequestId();
                String path = request.getRequestURI();

                logger.warn("Validation error occurred: {} | Path: {} | Method: {} | Errors: {} | RequestId: {}",
                                ex.getClass().getSimpleName(),
                                path,
                                request.getMethod(),
                                errors,
                                requestId);

                ApiErrorResponse<Void> errorResponse = ApiErrorResponse.error(
                                HttpStatus.BAD_REQUEST.value(),
                                "Dữ liệu đầu vào không hợp lệ",
                                "VALIDATION_ERROR",
                                errors,
                                path,
                                requestId);

                return ResponseEntity.badRequest().body(errorResponse);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ApiErrorResponse<Void>> handleConstraintViolationException(
                        ConstraintViolationException ex, HttpServletRequest request) {

                List<String> errors = ex.getConstraintViolations()
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.toList());

                String requestId = RequestContextUtil.getRequestId();
                String path = request.getRequestURI();

                logger.warn("Constraint violation occurred: {} | Path: {} | Method: {} | Errors: {} | RequestId: {}",
                                ex.getClass().getSimpleName(),
                                path,
                                request.getMethod(),
                                errors,
                                requestId);

                ApiErrorResponse<Void> errorResponse = ApiErrorResponse.error(
                                HttpStatus.BAD_REQUEST.value(),
                                "Dữ liệu yêu cầu không hợp lệ",
                                "CONSTRAINT_VIOLATION",
                                errors,
                                path,
                                requestId);

                return ResponseEntity.badRequest().body(errorResponse);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ApiErrorResponse<Void>> handleRuntimeException(RuntimeException ex,
                        HttpServletRequest request) {

                String requestId = RequestContextUtil.getRequestId();
                String path = request.getRequestURI();

                logger.error("RuntimeException occurred: {} - {} | Path: {} | Method: {} | RequestId: {} | Stack trace: ",
                                ex.getClass().getSimpleName(),
                                ex.getMessage(),
                                path,
                                request.getMethod(),
                                requestId,
                                ex);

                ApiErrorResponse<Void> errorResponse = ApiErrorResponse.error(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Lỗi runtime: " + ex.getMessage(),
                                "RUNTIME_ERROR",
                                path,
                                requestId);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse<Void>> handleGenericException(Exception ex, HttpServletRequest request) {

                String requestId = RequestContextUtil.getRequestId();
                String path = request.getRequestURI();

                logger.error("Unexpected exception occurred: {} - {} | Path: {} | Method: {} | RequestId: {} | Stack trace: ",
                                ex.getClass().getSimpleName(),
                                ex.getMessage(),
                                path,
                                request.getMethod(),
                                requestId,
                                ex);

                ApiErrorResponse<Void> errorResponse = ApiErrorResponse.error(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Đã xảy ra lỗi không mong muốn. Vui lòng thử lại sau.",
                                "INTERNAL_SERVER_ERROR",
                                path,
                                requestId);

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
}
