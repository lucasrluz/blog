package com.api.blog.dto.postDTO;

public class PostDTOEditResponse {
    public String postId;
    public String title;
    public String content;

    public PostDTOEditResponse(String postId, String title, String content) {
        this.postId = postId;
        this.title = title;
        this.content = content;
    }
}
