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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final UserRoleRepository userRoleRepository;
    private final UserRoleAccessRepository userRoleAccessRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager, UserRoleRepository userRoleRepository, UserRoleAccessRepository userRoleAccessRepository, UserRepository userRepository, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRoleRepository = userRoleRepository;
        this.userRoleAccessRepository = userRoleAccessRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> authLogin(@RequestBody AuthRequestDto dto) {
        Map<String, String> response = new HashMap<>();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        response.put("token", userService.authLogin(authentication));

        return ResponseEntity.ok().body(response);
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
