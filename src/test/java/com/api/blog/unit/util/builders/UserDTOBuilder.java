package com.api.blog.unit.util.builders;

import com.api.blog.dto.UserDTO;

public class UserDTOBuilder {
    public static UserDTO createValidUserDTO() {
        UserDTO userDTO = new UserDTO("name test", "nametest@gmail.com", "123");

        return userDTO;
    }

    public static UserDTO createUserDTOWithInvalidName() {
        UserDTO userDTO = new UserDTO("", "nametest@gmail.com", "123");

        return userDTO;
    }

    public static UserDTO createUserDTOWithInvalidEmail() {
        UserDTO userDTO = new UserDTO("name test", "@gmail.com", "123");

        return userDTO;
    }

    public static UserDTO createUserDTOWithInvalidPassword() {
        UserDTO userDTO = new UserDTO("name test", "nametest@gmail.com", "");

        return userDTO;
    }
}
