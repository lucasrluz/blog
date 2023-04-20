package com.api.blog.dto.postDTO;

public class PostDTORequest {
    public String title;
    public String content;

    public PostDTORequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
