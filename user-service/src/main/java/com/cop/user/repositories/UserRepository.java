package com.cop.user.repositories;

import com.cop.user.dtos.UserResponseDto;
import com.cop.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT new com.cop.user.dtos.UserResponseDto(u.id, u.username, " +
            "u.firstName, u.lastName, u.lastLoginDate) " +
            "FROM User u " +
            "WHERE u.deleted IS NULL OR u.deleted = :deleted")
    List<UserResponseDto> findAllUsers(Boolean deleted);
}
