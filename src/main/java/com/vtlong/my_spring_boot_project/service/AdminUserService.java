package com.vtlong.my_spring_boot_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vtlong.my_spring_boot_project.dto.request.CreateUserRequestDto;
import com.vtlong.my_spring_boot_project.dto.request.UpdateUserRequestDto;
import com.vtlong.my_spring_boot_project.dto.response.UserResponseDto;
import com.vtlong.my_spring_boot_project.exception.AppException;
import com.vtlong.my_spring_boot_project.mapper.UserMapper;
import com.vtlong.my_spring_boot_project.model.User;
import com.vtlong.my_spring_boot_project.repository.UserRepository;

@Service
@Transactional
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public AdminUserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.mapUsersToUserResponseDtos(users);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(String id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponseDto userResponseDto = userMapper.mapUserToUserResponseDto(user);
            return userResponseDto;
        } else {
            throw new AppException("User not found with id: " + id, Integer.valueOf(404));
        }
    }

    public UserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        if (userRepository.existsUserByEmail(createUserRequestDto.getEmail())) {
            throw new AppException("Email already exists: " + createUserRequestDto.getEmail(), Integer.valueOf(409));
        }

        User user = userMapper.mapCreateUserRequestDtoToUser(createUserRequestDto);
        User savedUser = userRepository.save(user);
        return userMapper.mapUserToUserResponseDto(savedUser);
    }

    public UserResponseDto updateUser(String id, UpdateUserRequestDto updateUserRequestDto) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            userMapper.mapUpdateUserRequestDtoToUser(updateUserRequestDto, user);
            User updatedUser = userRepository.save(user);
            return userMapper.mapUserToUserResponseDto(updatedUser);
        } else {
            throw new AppException("User not found with id: " + id, Integer.valueOf(404));
        }
    }

    public void deleteUser(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new AppException("User not found with id: " + id, Integer.valueOf(404));
        }
    }
}
