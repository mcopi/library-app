package com.cop.user.controllers;

import com.cop.user.dtos.AuthRequestDto;
import com.cop.user.repositories.UserRepository;
import com.cop.user.repositories.UserRoleAccessRepository;
import com.cop.user.repositories.UserRoleRepository;
import com.cop.user.services.JwtService;
import com.cop.user.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth-user")
public class AuthController {
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;
    private final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>> authLogin(@RequestBody AuthRequestDto dto) {
        log.info("Request login for username: {}", dto.getUsername());
        Map<String, String> response = new HashMap<>();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );

        response.put("token", jwtService.generateToken(authentication));

        return ResponseEntity.ok().body(response);
    }
}
