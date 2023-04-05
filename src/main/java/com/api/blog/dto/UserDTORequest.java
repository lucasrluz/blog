package com.api.blog.dto;

public class UserDTORequest {
    public String name;
    public String email;
    public String password;

    public UserDTORequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
