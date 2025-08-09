package com.cop.user.models;

import jakarta.persistence.*;

@Entity
@Table(name = "cop_role_access")
public class RoleAccess extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "access_name", length = 200)
    private String accessName;
    @Column(name = "access_code", length = 50)
    private String accessCode;

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
