package com.api.blog.unit.util.builders;

import com.api.blog.dto.UserDTOEditRequest;

public class UserDTOEditRequestBuilder {
    public static UserDTOEditRequest createValidUserDTOEditRequest() {
        UserDTOEditRequest userDTOEditRequest = new UserDTOEditRequest(null, "foo", "foo@gmail.com", "foo");

        return userDTOEditRequest;
    }

    public static UserDTOEditRequest createUserDTOEditRequestWithInvalidPassword() {
        UserDTOEditRequest userDTOEditRequest = new UserDTOEditRequest(null, "foo", "foo@gmail.com", "bar");

        return userDTOEditRequest;
    }

    public static UserDTOEditRequest createUserDTOEditRequestWithInvalidEmail() {
        UserDTOEditRequest userDTOEditRequest = new UserDTOEditRequest(null, "foo", "otheremail@gmail.com", "foo");

        return userDTOEditRequest;
    }
}
