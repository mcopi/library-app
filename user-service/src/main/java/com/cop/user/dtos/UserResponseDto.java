package com.cop.user.dtos;

import java.util.Date;

public class UserResponseDto {
    public UserResponseDto(Long userId, String username, String firstName, String lastName, Date lastLoginDate) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastLoginDate = lastLoginDate;
    }

    public UserResponseDto() {
    }

    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private Date lastLoginDate;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
