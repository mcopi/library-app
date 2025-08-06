package com.cop.user.services.impl;

import com.cop.user.dtos.AuthRequestDto;
import com.cop.user.models.User;
import com.cop.user.models.UserDetailModel;
import com.cop.user.repositories.UserRepository;
import com.cop.user.repositories.UserRoleAccessRepository;
import com.cop.user.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final UserRoleAccessRepository userRoleAccessRepository;

    public UserServiceImpl(UserRepository userRepository, UserRoleAccessRepository userRoleAccessRepository) {
        this.userRepository = userRepository;
        this.userRoleAccessRepository = userRoleAccessRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        UserDetailModel response = new UserDetailModel();

        Optional.of(user)
                .ifPresentOrElse(
                        d -> setUserDetailModel(d.get(), response),
                        () -> {
                            throw new UsernameNotFoundException("Username tidak ditemukan");
                        }
                );

        return response;
    }

    private void setUserDetailModel(User user, UserDetailModel response) {
        Set<GrantedAuthority> authorities = userRoleAccessRepository
                .findByRole_Id(user.getRoleId().getId())
                .stream()
                .map(d ->
                        new SimpleGrantedAuthority(d.getRoleAccess().getAccessName())
                ).collect(Collectors.toSet());

        response.setUsername(user.getUsername());
        response.setPassword(user.getPassword());
        response.setAuthorities(authorities);

        log.info("Access: {}", response.getAuthorities());
    }


}
