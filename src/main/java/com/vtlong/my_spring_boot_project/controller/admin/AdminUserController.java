package com.vtlong.my_spring_boot_project.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vtlong.my_spring_boot_project.dto.request.CreateUserRequestDto;
import com.vtlong.my_spring_boot_project.dto.request.UpdateUserRequestDto;
import com.vtlong.my_spring_boot_project.dto.response.ApiResponse;
import com.vtlong.my_spring_boot_project.dto.response.UserResponseDto;
import com.vtlong.my_spring_boot_project.service.AdminUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers(HttpServletRequest request) {
        List<UserResponseDto> users = adminUserService.getAllUsers();
        String path = request.getRequestURI();
        ApiResponse<List<UserResponseDto>> response = ApiResponse.<List<UserResponseDto>>builder()
                .status(200)
                .message("Lấy danh sách người dùng thành công")
                .data(users)
                .path(path)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable String id,
            HttpServletRequest request) {
        UserResponseDto user = adminUserService.getUserById(id);
        String path = request.getRequestURI();
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .status(200)
                .message("Lấy thông tin người dùng thành công")
                .data(user)
                .path(path)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @Valid @RequestBody CreateUserRequestDto createUserRequestDto, HttpServletRequest request) {
        UserResponseDto createdUser = adminUserService.createUser(createUserRequestDto);
        String path = request.getRequestURI();
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .status(201)
                .message("Tạo người dùng thành công")
                .data(createdUser)
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequestDto updateUserRequestDto, HttpServletRequest request) {
        UserResponseDto updatedUser = adminUserService.updateUser(id, updateUserRequestDto);
        String path = request.getRequestURI();
        ApiResponse<UserResponseDto> response = ApiResponse.<UserResponseDto>builder()
                .status(200)
                .message("Cập nhật người dùng thành công")
                .data(updatedUser)
                .path(path)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id, HttpServletRequest request) {
        adminUserService.deleteUser(id);
        String path = request.getRequestURI();
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(200)
                .message("Xóa người dùng thành công")
                .data(null)
                .path(path)
                .build();
        return ResponseEntity.ok(response);
    }
}
