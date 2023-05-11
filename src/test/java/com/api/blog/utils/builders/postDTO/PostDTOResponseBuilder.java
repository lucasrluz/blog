package com.api.blog.utils.builders.postDTO;

import java.util.UUID;

import com.api.blog.dto.postDTO.PostDTOResponse;

public class PostDTOResponseBuilder {
    public static PostDTOResponse createValidPostDTOResponse() {
        String postId = new UUID(0, 0).toString();
        String userId = new UUID(0, 0).toString();
        PostDTOResponse postDTOResponse = new PostDTOResponse(postId, userId, "foo", "foo");

        return postDTOResponse;
    }    
}
