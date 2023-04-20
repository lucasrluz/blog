package com.api.blog.unit.service;

import java.util.Optional;
import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.blog.domain.exceptions.InvalidDomainDataException;
import com.api.blog.dto.postDTO.PostDTORequest;
import com.api.blog.model.PostModel;
import com.api.blog.model.UserModel;
import com.api.blog.repositories.PostRepository;
import com.api.blog.repositories.UserRepository;
import com.api.blog.services.PostService;
import com.api.blog.services.util.BadRequestException;
import com.api.blog.unit.util.builders.PostDTORequestBuilder;
import com.api.blog.unit.util.builders.PostModelResponseBuilder;
import com.api.blog.unit.util.builders.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class PostServiceTests {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void esperoQueCrieUmPostComSucesso() throws BadRequestException, InvalidDomainDataException {
        UserModel userModel = UserModelBuilder.createValidUserModel();
        Optional<UserModel> optionalUserModel = Optional.of(userModel);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(optionalUserModel);
        
        PostModel postModel = PostModelResponseBuilder.createValidPostModelResponse();

        BDDMockito.when(this.postRepository.save(ArgumentMatchers.any())).thenReturn(postModel);

        PostDTORequest postDTORequest = PostDTORequestBuilder.createValidPostDTORequest();

        String response = this.postService.save(postDTORequest, userModel.getUserId().toString());

        Assertions.assertThat(response).isEqualTo(new UUID(0, 0).toString());
    }

    @Test
    public void esperoQueRetorneUmErroDeUsuarioNaoEncontrado() {
        Optional<UserModel> optionalUserModel = Optional.empty();
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(optionalUserModel);
        
        PostDTORequest postDTORequest = PostDTORequestBuilder.createValidPostDTORequest();
        
        String userId = new UUID(0, 0).toString();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.postService.save(postDTORequest, userId))
            .withMessage("Error: Usuário não encontrado");
    }
}
