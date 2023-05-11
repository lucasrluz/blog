package com.api.blog.integration.util;

import com.api.blog.model.PostModel;

public class PostModelBuilderIntegrationTests {
    public static PostModel createValidPostModel() {
        PostModel postModel = new PostModel("foo", "foo", null);

        return postModel;
    }
}
