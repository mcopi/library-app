package com.cop.user.services;

import com.cop.user.dtos.AuthRequestDto;

public interface UserService {
    String login(AuthRequestDto dto);
}
