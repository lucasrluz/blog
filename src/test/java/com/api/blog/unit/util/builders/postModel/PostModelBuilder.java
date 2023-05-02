package com.api.blog.unit.util.builders.postModel;

import com.api.blog.model.PostModel;

public class PostModelBuilder {
    public static PostModel createValidPostModel() {
        PostModel postModel = new PostModel("foo", "foo", null);

        return postModel;
    }
}
