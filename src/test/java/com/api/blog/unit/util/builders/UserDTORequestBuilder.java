package com.api.blog.unit.util.builders;

import com.api.blog.dto.UserDTORequest;

public class UserDTORequestBuilder {
    public static UserDTORequest createValidUserDTORequest() {
        UserDTORequest userDTORequest = new UserDTORequest("foo", "foo@gmail.com", "foo");

        return userDTORequest;
    }

    public static UserDTORequest createUserDTORequestWithInvalidName() {
        UserDTORequest userDTORequest = new UserDTORequest("", "foo@gmail.com", "foo");

        return userDTORequest;
    }

    public static UserDTORequest createUserDTORequestWithInvalidEmail() {
        UserDTORequest userDTORequest = new UserDTORequest("foo", "@gmail.com", "foo");

        return userDTORequest;
    }

    public static UserDTORequest createUserDTORequestWithInvalidPassword() {
        UserDTORequest userDTORequest = new UserDTORequest("foo", "foo@gmail.com", "");

        return userDTORequest;
    }
}
