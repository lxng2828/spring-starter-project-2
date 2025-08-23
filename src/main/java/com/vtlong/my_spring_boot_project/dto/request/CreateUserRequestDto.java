package com.vtlong.my_spring_boot_project.dto.request;

import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.vtlong.my_spring_boot_project.model.Gender;
import com.vtlong.my_spring_boot_project.model.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {
    private String email;
    private String password;
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String address;
    private Set<Role> roles;
}
