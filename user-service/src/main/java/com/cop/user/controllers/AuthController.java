package com.cop.user.controllers;

import com.cop.user.dtos.AuthRequestDto;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final UserRoleRepository userRoleRepository;
    private final UserRoleAccessRepository userRoleAccessRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService, UserRepository userRepository, UserRoleRepository userRoleRepository, UserRoleAccessRepository userRoleAccessRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userRoleAccessRepository = userRoleAccessRepository;
    }

    public ResponseEntity<String> authLogin(@RequestBody AuthRequestDto dto) {
        return ResponseEntity.ok()
                .body(userService.login(dto));
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
