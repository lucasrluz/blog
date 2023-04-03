package com.api.blog.services;

import com.api.blog.domain.UserDomain;
import com.api.blog.domain.exceptions.InvalidDomainDataException;
import com.api.blog.dto.UserDTO;
import com.api.blog.model.UserModel;
import com.api.blog.repositories.UserRepository;
import com.api.blog.services.util.BadRequestException;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserModel save(UserDTO userDTO) throws InvalidDomainDataException, BadRequestException {
        UserDomain.validate(userDTO.name, userDTO.email, userDTO.password);

        if (this.userRepository.existsByEmail(userDTO.email)) {
            throw new BadRequestException("Error: Este e-mail já está cadastrado");
        }

        UserModel userModel = new UserModel(userDTO.name, userDTO.email, userDTO.password);

        return this.userRepository.save(userModel); 
    }
}
