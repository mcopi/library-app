package com.cop.user.services;


import org.springframework.security.core.Authentication;

public interface UserService {
    String authLogin(Authentication authentication);
}
