package com.vtlong.my_spring_boot_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vtlong.my_spring_boot_project.model.User;
import com.vtlong.my_spring_boot_project.model.RoleType;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserByEmail(String email);

    boolean existsUserByEmail(String email);

    List<User> findUsersByRoleName(RoleType roleName);

    List<User> findUsersByNameContainingIgnoreCase(String name);
}
