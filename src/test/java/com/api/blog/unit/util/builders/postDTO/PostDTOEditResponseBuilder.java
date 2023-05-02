package com.api.blog.unit.util.builders.postDTO;

import java.util.UUID;

import com.api.blog.dto.postDTO.PostDTOEditResponse;

public class PostDTOEditResponseBuilder {
    public static PostDTOEditResponse createValidPostDTOEditResponse() {
        String postId = new UUID(0, 0).toString();

        PostDTOEditResponse postDTOEditResponse = new PostDTOEditResponse(postId, "foo", "foo");

        return postDTOEditResponse;
    }
}
