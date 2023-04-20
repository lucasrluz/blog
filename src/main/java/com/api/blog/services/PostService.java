package com.api.blog.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.api.blog.domain.PostDomain;
import com.api.blog.domain.exceptions.InvalidDomainDataException;
import com.api.blog.dto.postDTO.PostDTORequest;
import com.api.blog.model.PostModel;
import com.api.blog.model.UserModel;
import com.api.blog.repositories.PostRepository;
import com.api.blog.repositories.UserRepository;
import com.api.blog.services.util.BadRequestException;

@Service
public class PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public String save(PostDTORequest postDTORequest, String userId) throws InvalidDomainDataException, BadRequestException {
        PostDomain.validate(postDTORequest.title, postDTORequest.content);

        UUID uuidUserId = UUID.fromString(userId);

        Optional<UserModel> findUserModelByUserId = this.userRepository.findById(uuidUserId);

        if (!findUserModelByUserId.isPresent()) {
            throw new BadRequestException("Error: Usuário não encontrado");
        }

        PostModel postModel = new PostModel(postDTORequest.title, postDTORequest.content, findUserModelByUserId.get());

        PostModel savePostModel = this.postRepository.save(postModel);

        return savePostModel.getPostId().toString();
    }
}
