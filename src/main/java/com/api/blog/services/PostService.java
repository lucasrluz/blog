package com.api.blog.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.api.blog.domain.PostDomain;
import com.api.blog.domain.exceptions.InvalidDomainDataException;
import com.api.blog.dto.postDTO.PostDTOEditResponse;
import com.api.blog.dto.postDTO.PostDTORequest;
import com.api.blog.dto.postDTO.PostDTOResponse;
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

    public PostDTOEditResponse edit(String postId, PostDTORequest postDTORequest) throws BadRequestException, InvalidDomainDataException {
        PostDomain.validate(postDTORequest.title, postDTORequest.content);
        
        UUID uuidPostId = UUID.fromString(postId);

        Optional<PostModel> findPostByPostIdResponse = this.postRepository.findById(uuidPostId);

        if (!findPostByPostIdResponse.isPresent()) {
            throw new BadRequestException("Error: Post não encontrado");
        }

        PostModel postModel = findPostByPostIdResponse.get();

        postModel.setTitle(postDTORequest.title);
        postModel.setContent(postDTORequest.content);

        PostModel response = this.postRepository.save(postModel);

        return new PostDTOEditResponse(
            response.getPostId().toString(),
            response.getTitle(),
            response.getContent()
        );
    }

    public PostDTOResponse getByPostId(String postId) throws BadRequestException {
        UUID uuid = UUID.fromString(postId);

        Optional<PostModel> posts = this.postRepository.findById(uuid);

        if (!posts.isPresent()) {
            throw new BadRequestException("Error: Post não encontrado");
        }

        return new PostDTOResponse(
            posts.get().getPostId().toString(),
            posts.get().getUserModel().getUserId().toString(),
            posts.get().getTitle(),
            posts.get().getContent()
        );
    }
}
