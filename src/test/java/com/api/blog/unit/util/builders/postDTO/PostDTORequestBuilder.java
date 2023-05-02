package com.api.blog.unit.util.builders.postDTO;

import com.api.blog.dto.postDTO.PostDTORequest;

public class PostDTORequestBuilder {
    public static PostDTORequest createValidPostDTORequest() {
        PostDTORequest postDTORequest = new PostDTORequest("foo", "foo");

        return postDTORequest;
    }

    public static PostDTORequest createPostDTORequestWithInvalidTitle() {
        PostDTORequest postDTORequest = new PostDTORequest("", "foo");

        return postDTORequest;
    }

    public static PostDTORequest createPostDTORequestWithInvalidContent() {
        PostDTORequest postDTORequest = new PostDTORequest("foo", "");

        return postDTORequest;
    }
}
