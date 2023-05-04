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
import com.api.blog.dto.postDTO.PostDTOEditResponse;
import com.api.blog.dto.postDTO.PostDTORequest;
import com.api.blog.model.PostModel;
import com.api.blog.model.UserModel;
import com.api.blog.repositories.PostRepository;
import com.api.blog.repositories.UserRepository;
import com.api.blog.services.PostService;
import com.api.blog.services.util.BadRequestException;
import com.api.blog.utils.builders.postDTO.PostDTORequestBuilder;
import com.api.blog.unit.util.builders.postModel.PostModelResponseBuilder;
import com.api.blog.unit.util.builders.userModel.UserModelBuilderUnitTests;

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
        UserModel userModel = UserModelBuilderUnitTests.createValidUserModel();
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

    // edit

    @Test
    public void esperoQueEditeOTituloDoPost() throws BadRequestException, InvalidDomainDataException {
        PostModel postModelMock = PostModelResponseBuilder.createValidPostModelResponse();
        Optional<PostModel> optionalPostModelMock = Optional.of(postModelMock);
        BDDMockito.when(this.postRepository.findById(ArgumentMatchers.any())).thenReturn(optionalPostModelMock);
    
        PostModel postModelEditMock = PostModelResponseBuilder.createValidPostModelResponse();
        postModelEditMock.setTitle("baz");
        BDDMockito.when(this.postRepository.save(ArgumentMatchers.any())).thenReturn(postModelEditMock);

        PostDTORequest postDTORequest = PostDTORequestBuilder.createValidPostDTORequest();
        postDTORequest.title = "baz";

        PostDTOEditResponse postDTOEditResponse = this.postService.edit(postModelMock.getPostId().toString(), postDTORequest);
    
        Assertions.assertThat(postDTOEditResponse.postId).isEqualTo(postModelEditMock.getPostId().toString());
        Assertions.assertThat(postDTOEditResponse.title).isEqualTo(postModelEditMock.getTitle());
        Assertions.assertThat(postDTOEditResponse.content).isEqualTo(postModelEditMock.getContent());
    }

    @Test
    public void esperoQueRetorneUmErroDePostNaoEncontrado() throws BadRequestException, InvalidDomainDataException {
        PostModel postModelMock = PostModelResponseBuilder.createValidPostModelResponse();
        Optional<PostModel> optionalPostModelMock = Optional.empty();
        BDDMockito.when(this.postRepository.findById(ArgumentMatchers.any())).thenReturn(optionalPostModelMock);

        PostDTORequest postDTORequest = PostDTORequestBuilder.createValidPostDTORequest();
        postDTORequest.title = "baz";
    
        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> this.postService.edit(postModelMock.getPostId().toString(), postDTORequest))
            .withMessage("Error: Post não encontrado");
    }
}
