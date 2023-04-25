package com.api.blog.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.blog.domain.exceptions.InvalidDomainDataException;
import com.api.blog.dto.postDTO.PostDTOEditResponse;
import com.api.blog.dto.postDTO.PostDTORequest;
import com.api.blog.services.PostService;
import com.api.blog.services.util.BadRequestException;

@RestController
@RequestMapping("/post")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Object> save(@PathVariable String userId, @RequestBody PostDTORequest postDTORequest) {
        try {
            String postId = this.postService.save(postDTORequest, userId);

            return ResponseEntity.status(HttpStatus.CREATED).body(postId);
        } catch (BadRequestException badRequestException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(badRequestException.getMessage());
        } catch (InvalidDomainDataException invalidDomainDataException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(invalidDomainDataException.getMessage());
        }
    }

    @PutMapping("/{postId}")
    public ResponseEntity<Object> edit(@PathVariable String postId, @RequestBody PostDTORequest postDTORequest) {
        try {
            PostDTOEditResponse postDTOEditResponse = this.postService.edit(postId, postDTORequest);

            return ResponseEntity.status(HttpStatus.OK).body(postDTOEditResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
