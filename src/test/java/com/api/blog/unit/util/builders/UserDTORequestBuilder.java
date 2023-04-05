package com.api.blog.unit.util.builders;

import com.api.blog.dto.UserDTORequest;

public class UserDTORequestBuilder {
    public static UserDTORequest createValidUserDTORequest() {
        UserDTORequest userDTORequest = new UserDTORequest("name test", "nametest@gmail.com", "123");

        return userDTORequest;
    }

    public static UserDTORequest createUserDTORequestWithInvalidName() {
        UserDTORequest userDTORequest = new UserDTORequest("", "nametest@gmail.com", "123");

        return userDTORequest;
    }

    public static UserDTORequest createUserDTORequestWithInvalidEmail() {
        UserDTORequest userDTORequest = new UserDTORequest("name test", "@gmail.com", "123");

        return userDTORequest;
    }

    public static UserDTORequest createUserDTORequestWithInvalidPassword() {
        UserDTORequest userDTORequest = new UserDTORequest("name test", "nametest@gmail.com", "");

        return userDTORequest;
    }
}
