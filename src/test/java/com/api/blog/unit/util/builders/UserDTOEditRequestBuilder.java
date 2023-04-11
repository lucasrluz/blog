package com.api.blog.unit.util.builders;

import com.api.blog.dto.UserDTOEditRequest;

public class UserDTOEditRequestBuilder {
    public static UserDTOEditRequest createValidUserDTOEditRequest() {
        UserDTOEditRequest userDTOEditRequest = new UserDTOEditRequest(null, "new test", "nametest@gmail.com", "123");

        return userDTOEditRequest;
    }
}
