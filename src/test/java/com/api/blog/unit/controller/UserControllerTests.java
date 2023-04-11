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

import com.api.blog.controllers.UserController;
import com.api.blog.domain.exceptions.InvalidDomainDataException;
import com.api.blog.dto.UserDTOEditRequest;
import com.api.blog.dto.UserDTOEditResponse;
import com.api.blog.dto.UserDTORequest;
import com.api.blog.dto.UserDTOResponse;
import com.api.blog.services.UserService;
import com.api.blog.services.util.BadRequestException;
import com.api.blog.unit.util.builders.UserDTOEditRequestBuilder;
import com.api.blog.unit.util.builders.UserDTOEditResponseBuilder;
import com.api.blog.unit.util.builders.UserDTORequestBuilder;
import com.api.blog.unit.util.builders.UserDTOResponseBuilder;

@ExtendWith(SpringExtension.class)
public class UserControllerTests {
    @InjectMocks
    private UserController userController;
    
    @Mock
    private UserService userService;

    @Test
    public void esperoQueRetorneUmCodigoDeStatus201EONovoUsuarioCriado() throws InvalidDomainDataException, BadRequestException {
        UserDTOResponse userDTOResponse = UserDTOResponseBuilder.createValidUserDTO();
        BDDMockito.when(userService.save(ArgumentMatchers.any())).thenReturn(userDTOResponse);

        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();

        ResponseEntity<Object> responseEntity = this.userController.save(userDTORequest);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(userDTOResponse);
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus401ComUmaExcecaoDeInvalidDomainException() throws InvalidDomainDataException, BadRequestException {
        InvalidDomainDataException invalidDomainDataException = new InvalidDomainDataException("name: must not be blank");
        BDDMockito.when(userService.save(ArgumentMatchers.any())).thenThrow(invalidDomainDataException);
        
        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();
        ResponseEntity<Object> responseEntity = this.userController.save(userDTORequest);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("name: must not be blank");
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus401ComUmaExcecaoBadRequest() throws InvalidDomainDataException, BadRequestException {
        BadRequestException badRequestException = new BadRequestException("Error: Este e-mail já está cadastrado");
        BDDMockito.when(userService.save(ArgumentMatchers.any())).thenThrow(badRequestException);
        
        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();
        ResponseEntity<Object> responseEntity = this.userController.save(userDTORequest);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Error: Este e-mail já está cadastrado");
    }

    // GET /user/{id}

    @Test
    public void esperoQueRetornOUsuarioCorretoPeloId() throws BadRequestException {
        UserDTOResponse userDTOResponse = UserDTOResponseBuilder.createValidUserDTO();
        BDDMockito.when(userService.getByUserId(ArgumentMatchers.any())).thenReturn(userDTOResponse);

        ResponseEntity<Object> responseEntity = this.userController.getByUserId(userDTOResponse.userId.toString());

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(userDTOResponse);
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus401ComUmaExcecaoBadRequestDadoUmIdInvalido() throws InvalidDomainDataException, BadRequestException {
        BadRequestException badRequestException = new BadRequestException("Error: Usuário não encontrado");
        BDDMockito.when(userService.getByUserId(ArgumentMatchers.any())).thenThrow(badRequestException);
        
        UUID userId = UserDTOResponseBuilder.createValidUserDTO().userId;
        ResponseEntity<Object> responseEntity = this.userController.getByUserId(userId.toString());

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Error: Usuário não encontrado");
    }

    //  PUT /user/{userId}

    @Test
    public void esperoQueEditeOUsuarioPeloIdInformado() throws BadRequestException {
        UserDTOEditResponse userDTOEditResponse = UserDTOEditResponseBuilder.createValidUserDTOEditResponse();
        BDDMockito.when(userService.edit(ArgumentMatchers.any())).thenReturn(userDTOEditResponse);

        UserDTOEditRequest userDTOEditRequest = UserDTOEditRequestBuilder.createValidUserDTOEditRequest();

        ResponseEntity<Object> responseEntity = this.userController.edit(userDTOEditResponse.userId.toString(), userDTOEditRequest);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(userDTOEditResponse);
    }

    @Test
    public void esperoQueRetorneUmErroDeUsuarioNaoEncontrado() throws BadRequestException {
        BadRequestException badRequestException = new BadRequestException("Error: Usuário não encontrado");
        BDDMockito.when(userService.edit(ArgumentMatchers.any())).thenThrow(badRequestException);

        UserDTOEditRequest userDTOEditRequest = UserDTOEditRequestBuilder.createValidUserDTOEditRequest();

        ResponseEntity<Object> responseEntity = this.userController.edit("7f4baffd-96f8-4b5a-b1de-84437519ffc2", userDTOEditRequest);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Error: Usuário não encontrado");
    }
}
