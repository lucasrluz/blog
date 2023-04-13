package com.api.blog.unit.util.builders;

import java.util.UUID;

import com.api.blog.model.UserModel;

public class UserModelBuilder {
    public static UserModel createValidUserModel() {
        UUID uuid = new UUID(0, 0);
        UserModel userModel = new UserModel("foo", "foo@gmail.com", "foo");
        userModel.setUserId(uuid);

        return userModel;
    }
}
