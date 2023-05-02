package com.api.blog.integration.util;

import com.api.blog.model.UserModel;

public class UserModelBuilderIntegrationTests {
    public static UserModel createValidUserModel() {
        UserModel userModel = new UserModel("foo", "foo@gmail.com", "foo");

        return userModel;
    }
}
