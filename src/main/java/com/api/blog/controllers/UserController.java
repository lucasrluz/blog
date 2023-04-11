package com.api.blog.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.blog.dto.UserDTOEditRequest;
import com.api.blog.dto.UserDTOEditResponse;
import com.api.blog.dto.UserDTORequest;
import com.api.blog.dto.UserDTOResponse;
import com.api.blog.services.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseEntity<Object> save(@RequestBody UserDTORequest userDTORequest) {
        try {
            UserDTOResponse userDTOResponse = this.userService.save(userDTORequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(userDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getByUserId(@PathVariable String userId) {
        try {
            UserDTOResponse userDTOResponse = this.userService.getByUserId(userId);

            return ResponseEntity.status(HttpStatus.OK).body(userDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> edit(@PathVariable String userId, @RequestBody UserDTOEditRequest userDTOEditRequest) {
        try {
            userDTOEditRequest.userId = userId;

            UserDTOEditResponse userDTOEditResponse = this.userService.edit(userDTOEditRequest);

            return ResponseEntity.status(HttpStatus.OK).body(userDTOEditResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
