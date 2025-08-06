package com.cop.user.repositories;

import com.cop.user.models.UserRoleAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleAccessRepository extends JpaRepository<UserRoleAccess, Long> {
    @Query("SELECT ura FROM UserRoleAccess ura " +
            "JOIN FETCH ura.roleAccess ra " +
            "JOIN FETCH ura.role r " +
            "WHERE r.id = :id")
    List<UserRoleAccess> findByRole_Id(@Param("id") Long id);
}
