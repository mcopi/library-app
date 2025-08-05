package com.cop.user.services.impl;

import com.cop.user.dtos.AuthRequestDto;
import com.cop.user.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public String login(AuthRequestDto dto) {
        return "";
    }
}
