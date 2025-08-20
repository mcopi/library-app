package com.cop.user.controllers;

import com.cop.user.dtos.UserRequestDto;
import com.cop.user.dtos.UserResponseDto;
import com.cop.user.models.User;
import com.cop.user.models.UserRole;
import com.cop.user.models.UserRoleAccess;
import com.cop.user.repositories.UserRepository;
import com.cop.user.repositories.UserRoleAccessRepository;
import com.cop.user.repositories.UserRoleRepository;
import com.cop.user.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user")
public class UsersController {
    private Logger log = LoggerFactory.getLogger(UsersController.class);
    private final UserService userService;
    private final UserRoleRepository userRoleRepository;
    private final UserRoleAccessRepository userRoleAccessRepository;
    private final UserRepository userRepository;

    public UsersController(UserService userService, UserRoleRepository userRoleRepository, UserRoleAccessRepository userRoleAccessRepository, UserRepository userRepository) {
        this.userService = userService;
        this.userRoleRepository = userRoleRepository;
        this.userRoleAccessRepository = userRoleAccessRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(@RequestBody UserRequestDto dto,
                                                             Authentication auth) {
        log.info("Request get all users by: {}", auth.getName());
        return ResponseEntity.ok().body(userService.findAllUsers(dto));
    }

    @GetMapping(value = "/by")
    public ResponseEntity<String> listTesting(@RequestParam Long id) {
        Optional<User> user = userRepository.findById(id);
        Optional.of(user)
                .ifPresent(d -> {
                    Optional<UserRole> userRole = userRoleRepository.findById(d.get().getRoleId().getId());
                    List<UserRoleAccess> accessList = userRoleAccessRepository.findByRole_Id(userRole.get().getId());
                    accessList.forEach(a -> log.info("Access: {}", a.getRoleAccess().getAccessName()));
                });
        return ResponseEntity.ok()
                .body("UserDto: {}");
    }
}
