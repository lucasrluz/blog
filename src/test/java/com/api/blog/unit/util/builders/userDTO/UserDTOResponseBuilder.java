package com.api.blog.unit.util.builders.userDTO;

import java.util.UUID;

import com.api.blog.dto.UserDTOResponse;

public class UserDTOResponseBuilder {
    public static UserDTOResponse createValidUserDTO() {
        UUID uuid = new UUID(0, 0);
        UserDTOResponse userDTOResponse = new UserDTOResponse(uuid, "foo", "foo@gmail.com", "foo");

        return userDTOResponse;
    }

    public static UserDTOResponse createUserDTOWithInvalidName() {
        UUID uuid = new UUID(0, 0);
        UserDTOResponse userDTOResponse = new UserDTOResponse(uuid, "", "foo@gmail.com", "foo");

        return userDTOResponse;
    }

    public static UserDTOResponse createUserDTOWithInvalidEmail() {
        UUID uuid = new UUID(0, 0);
        UserDTOResponse userDTOResponse = new UserDTOResponse(uuid, "foo", "@gmail.com", "foo");

        return userDTOResponse;
    }

    public static UserDTOResponse createUserDTOWithInvalidPassword() {
        UUID uuid = new UUID(0, 0);
        UserDTOResponse userDTOResponse = new UserDTOResponse(uuid, "foo", "foo@gmail.com", "");

        return userDTOResponse;
    }
}
