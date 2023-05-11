package com.api.blog.dto.postDTO;

public class PostDTOResponse {
    public String postId;
    public String userId;
    public String title;
    public String content;

    public PostDTOResponse(String postId, String userId, String title, String content) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }
}
