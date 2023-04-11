package com.api.blog.dto;

public class UserDTOEditRequest {
    public String userId;
    public String newName;
    public String email;
    public String password;

    public UserDTOEditRequest(String userId, String newName, String email, String password) {
        this.userId = userId;
        this.newName = newName;
        this.email = email;
        this.password = password;
    }
}
