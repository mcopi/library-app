package com.cop.user.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cop_user_role_access")
public class UserRoleAccess extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private UserRole role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_access_id")
    private RoleAccess roleAccess;

    public UserRole getRoleId() {
        return role;
    }

    public void setRoleId(UserRole role) {
        this.role = role;
    }

    public RoleAccess getRoleAccess() {
        return roleAccess;
    }

    public void setRoleAccess(RoleAccess roleAccess) {
        this.roleAccess = roleAccess;
    }
}
