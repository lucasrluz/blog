package com.api.blog.unit.util.builders;

import com.api.blog.dto.UserDTO;

public class UserDTOBuilder {
    public static UserDTO createValidUserDTO() {
        UserDTO userDTO = new UserDTO("name test", "nametest@gmail.com", "123");

        return userDTO;
    }
}
