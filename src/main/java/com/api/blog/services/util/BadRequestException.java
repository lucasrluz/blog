package com.api.blog.services.util;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
