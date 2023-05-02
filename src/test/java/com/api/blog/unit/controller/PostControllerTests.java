package com.api.blog.unit.controller;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.blog.controllers.PostController;
import com.api.blog.domain.exceptions.InvalidDomainDataException;
import com.api.blog.dto.postDTO.PostDTOEditResponse;
import com.api.blog.dto.postDTO.PostDTORequest;
import com.api.blog.services.PostService;
import com.api.blog.services.util.BadRequestException;
import com.api.blog.unit.util.builders.postDTO.PostDTOEditResponseBuilder;
import com.api.blog.unit.util.builders.postDTO.PostDTORequestBuilder;

@ExtendWith(SpringExtension.class)
public class PostControllerTests {
    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @Test
    public void esperoQueRetorneUmCodigoDeStatus201EUmUUID() throws BadRequestException, InvalidDomainDataException {
        String postId = new UUID(0, 0).toString();
        BDDMockito.when(this.postService.save(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(postId);

        PostDTORequest postDTORequest = PostDTORequestBuilder.createValidPostDTORequest();

        String userId = new UUID(0, 0).toString();

        ResponseEntity<Object> response = this.postController.save(userId ,postDTORequest);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isEqualTo(postId);
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus404ComUmaMensagemDeUsuarioNaoEncontrado() throws BadRequestException, InvalidDomainDataException {
        BadRequestException badRequestException = new BadRequestException("Error: Usuário não encontrado");
        BDDMockito.when(this.postService.save(ArgumentMatchers.any(), ArgumentMatchers.any())).thenThrow(badRequestException);
        
        PostDTORequest postDTORequest = PostDTORequestBuilder.createValidPostDTORequest();
        
        String userId = new UUID(0, 0).toString();

        ResponseEntity<Object> response = this.postController.save(userId, postDTORequest);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(response.getBody()).isEqualTo("Error: Usuário não encontrado");
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmaMensagemDeDadoDeEntradaInvalido() throws BadRequestException, InvalidDomainDataException {
        InvalidDomainDataException invalidDomainDataException = new InvalidDomainDataException("title: must not be a blank");
        BDDMockito.when(this.postService.save(ArgumentMatchers.any(), ArgumentMatchers.any())).thenThrow(invalidDomainDataException);
        
        PostDTORequest postDTORequest = PostDTORequestBuilder.createPostDTORequestWithInvalidTitle();
        
        String userId = new UUID(0, 0).toString();

        ResponseEntity<Object> response = this.postController.save(userId, postDTORequest);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isEqualTo("title: must not be a blank");
    }

    // PUT /post/{postId}

    @Test
    public void esperoQueRetorneUmCodigoDeStatus200ComDadosDoPost() throws BadRequestException, InvalidDomainDataException {
        PostDTOEditResponse postDTOEditResponseMock = PostDTOEditResponseBuilder.createValidPostDTOEditResponse();
        BDDMockito.when(this.postService.edit(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(postDTOEditResponseMock);

        String postId = postDTOEditResponseMock.postId;

        PostDTORequest postDTORequest = PostDTORequestBuilder.createValidPostDTORequest();

        ResponseEntity<Object> response = this.postController.edit(postId, postDTORequest);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(postDTOEditResponseMock);
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus404EUmErroDePostNaoEncontrado() throws BadRequestException, InvalidDomainDataException {
        BadRequestException badRequestExceptionMock = new BadRequestException("Error: Post não encontrado");
        BDDMockito.when(this.postService.edit(ArgumentMatchers.any(), ArgumentMatchers.any())).thenThrow(badRequestExceptionMock);

        String postId = new UUID(0, 0).toString();

        PostDTORequest postDTORequest = PostDTORequestBuilder.createValidPostDTORequest();

        ResponseEntity<Object> response = this.postController.edit(postId, postDTORequest);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(response.getBody()).isEqualTo("Error: Post não encontrado");
    }
}
