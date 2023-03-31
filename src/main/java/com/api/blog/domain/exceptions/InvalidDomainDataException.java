package com.api.blog.domain.exceptions;

public class InvalidDomainDataException extends Exception {
    public InvalidDomainDataException(String message) {
        super(message);
    }
}
