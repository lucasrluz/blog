package com.api.blog.unit.util.builders;

import java.util.UUID;

import com.api.blog.dto.UserDTOResponse;

public class UserDTOResponseBuilder {
    public static UserDTOResponse createValidUserDTO() {
        UUID uuid = new UUID(0, 0);
        UserDTOResponse userDTOResponse = new UserDTOResponse(uuid, "name test", "nametest@gmail.com", "123");

        return userDTOResponse;
    }

    public static UserDTOResponse createUserDTOWithInvalidName() {
        UUID uuid = new UUID(0, 0);
        UserDTOResponse userDTOResponse = new UserDTOResponse(uuid, "", "nametest@gmail.com", "123");

        return userDTOResponse;
    }

    public static UserDTOResponse createUserDTOWithInvalidEmail() {
        UUID uuid = new UUID(0, 0);
        UserDTOResponse userDTOResponse = new UserDTOResponse(uuid, "name test", "@gmail.com", "123");

        return userDTOResponse;
    }

    public static UserDTOResponse createUserDTOWithInvalidPassword() {
        UUID uuid = new UUID(0, 0);
        UserDTOResponse userDTOResponse = new UserDTOResponse(uuid, "name test", "nametest@gmail.com", "");

        return userDTOResponse;
    }
}
