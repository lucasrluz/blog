package com.api.blog.domain;

import java.util.Set;
import jakarta.validation.Validator;

import com.api.blog.domain.exceptions.InvalidDomainDataException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;

public class PostDomain {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private PostDomain(@NotBlank String title, @NotBlank String content) {
        this.title = title;
        this.content = content;
    }

    public static PostDomain validate(String title, String content) throws InvalidDomainDataException {
        try {
            PostDomain postDomain = new PostDomain(title, content);
            
            validation(postDomain);

            return postDomain;
        } catch (ConstraintViolationException exception) {
            String errorMessage = exception.getMessage();

            throw new InvalidDomainDataException(errorMessage);
        }
    }

    private static void validation(PostDomain postDomain) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        final Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<PostDomain>> constraintViolations = validator.validate(postDomain);

        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }    
}
