package com.api.blog.services;

import org.springframework.stereotype.Service;

import com.api.blog.domain.UserDomain;
import com.api.blog.domain.exceptions.InvalidDomainDataException;
import com.api.blog.dto.UserDTORequest;
import com.api.blog.dto.UserDTOResponse;
import com.api.blog.model.UserModel;
import com.api.blog.repositories.UserRepository;
import com.api.blog.services.util.BadRequestException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTOResponse save(UserDTORequest userDTORequest) throws InvalidDomainDataException, BadRequestException {
        UserDomain.validate(userDTORequest.name, userDTORequest.email, userDTORequest.password);

        if (this.userRepository.existsByEmail(userDTORequest.email)) {
            throw new BadRequestException("Error: Este e-mail já está cadastrado");
        }

        UserModel userModel = new UserModel(userDTORequest.name, userDTORequest.email, userDTORequest.password);

        UserModel saveUserModelResponse = this.userRepository.save(userModel);
        
        return new UserDTOResponse(
            saveUserModelResponse.getUserId(),
            saveUserModelResponse.getName(),
            saveUserModelResponse.getEmail(),
            saveUserModelResponse.getPassword());
    }
}
