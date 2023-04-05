package com.api.blog.dto;

import java.util.UUID;

public class UserDTOResponse {
    public UUID userId;
    public String name;
    public String email;
    public String password;

    public UserDTOResponse(UUID userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
