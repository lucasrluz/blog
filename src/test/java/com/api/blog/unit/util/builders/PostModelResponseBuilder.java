package com.api.blog.unit.util.builders;

import java.util.UUID;

import com.api.blog.model.PostModel;
import com.api.blog.model.UserModel;

public class PostModelResponseBuilder {
    public static PostModel createValidPostModelResponse() {
        UserModel userModel = UserModelBuilder.createValidUserModel();

        PostModel postModel = new PostModel("foo", "foo", userModel);

        UUID uuid = new UUID(0, 0);

        postModel.setPostId(uuid);

        return postModel;
    }
}
