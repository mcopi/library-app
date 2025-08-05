package com.cop.user.repositories;

import com.cop.user.models.UserRoleAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleAccessRepository extends JpaRepository<UserRoleAccess, Long> {
    List<UserRoleAccess> findByRole_Id(Long id);
}
