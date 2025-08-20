package com.cop.user.services;


import com.cop.user.dtos.UserRequestDto;
import com.cop.user.dtos.UserResponseDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    List<UserResponseDto> findAllUsers(UserRequestDto dto);
}
