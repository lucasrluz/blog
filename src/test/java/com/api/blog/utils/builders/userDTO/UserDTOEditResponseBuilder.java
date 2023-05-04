package com.api.blog.utils.builders.userDTO;

import java.util.UUID;

import com.api.blog.dto.UserDTOEditResponse;

public class UserDTOEditResponseBuilder {
    public static UserDTOEditResponse createValidUserDTOEditResponse() {
        UUID uuid = new UUID(0, 0);
        UserDTOEditResponse userDTOEditResponse = new UserDTOEditResponse(uuid.toString(), "foo", "foo@gmail.com", "foo");
        
        return userDTOEditResponse;
    }
}
