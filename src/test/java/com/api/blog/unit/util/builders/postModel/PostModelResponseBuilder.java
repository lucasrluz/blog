package com.api.blog.unit.util.builders.postModel;

import java.util.UUID;

import com.api.blog.model.PostModel;
import com.api.blog.model.UserModel;
import com.api.blog.unit.util.builders.userModel.UserModelBuilderUnitTests;

public class PostModelResponseBuilder {
    public static PostModel createValidPostModelResponse() {
        UserModel userModel = UserModelBuilderUnitTests.createValidUserModel();

        PostModel postModel = new PostModel("foo", "foo", userModel);

        UUID uuid = new UUID(0, 0);

        postModel.setPostId(uuid);

        return postModel;
    }
}
