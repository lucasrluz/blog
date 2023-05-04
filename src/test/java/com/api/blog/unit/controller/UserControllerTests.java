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
import com.api.blog.utils.builders.userDTO.UserDTOEditRequestBuilder;
import com.api.blog.utils.builders.userDTO.UserDTOEditResponseBuilder;
import com.api.blog.utils.builders.userDTO.UserDTORequestBuilder;
import com.api.blog.utils.builders.userDTO.UserDTOResponseBuilder;

@ExtendWith(SpringExtension.class)
public class UserControllerTests {
    @InjectMocks
    private UserController userController;
    
    @Mock
    private UserService userService;

    @Test
    public void esperoQueRetorneUmCodigoDeStatus201EONovoUsuarioCriado() throws InvalidDomainDataException, BadRequestException {
        UserDTOResponse userDTOResponseMock = UserDTOResponseBuilder.createValidUserDTO();
        BDDMockito.when(userService.save(ArgumentMatchers.any())).thenReturn(userDTOResponseMock);

        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();

        ResponseEntity<Object> responseEntity = this.userController.save(userDTORequest);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(userDTOResponseMock);
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmaExcecaoBadRequest() throws InvalidDomainDataException, BadRequestException {
        BadRequestException badRequestExceptionMock = new BadRequestException("Error: Este e-mail já está cadastrado");
        BDDMockito.when(userService.save(ArgumentMatchers.any())).thenThrow(badRequestExceptionMock);
        
        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();
        ResponseEntity<Object> responseEntity = this.userController.save(userDTORequest);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Error: Este e-mail já está cadastrado");
    }

    // GET /user/{id}

    @Test
    public void esperoQueRetorneOUsuarioPeloIdInformado() throws BadRequestException {
        UserDTOResponse userDTOResponseMock = UserDTOResponseBuilder.createValidUserDTO();
        BDDMockito.when(userService.getByUserId(ArgumentMatchers.any())).thenReturn(userDTOResponseMock);

        ResponseEntity<Object> responseEntity = this.userController.getByUserId(userDTOResponseMock.userId.toString());

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(userDTOResponseMock);
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmaExcecaoBadRequestDadoUmIdInvalido() throws InvalidDomainDataException, BadRequestException {
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
        UserDTOEditResponse userDTOEditResponseMock = UserDTOEditResponseBuilder.createValidUserDTOEditResponse();
        BDDMockito.when(userService.edit(ArgumentMatchers.any())).thenReturn(userDTOEditResponseMock);

        UserDTOEditRequest userDTOEditRequest = UserDTOEditRequestBuilder.createValidUserDTOEditRequest();

        ResponseEntity<Object> responseEntity = this.userController.edit(userDTOEditResponseMock.userId.toString(), userDTOEditRequest);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(userDTOEditResponseMock);
    }

    @Test
    public void esperoQueRetorneUmErroDeUsuarioNaoEncontrado() throws BadRequestException {
        BadRequestException badRequestExceptionMock = new BadRequestException("Error: Usuário não encontrado");
        BDDMockito.when(userService.edit(ArgumentMatchers.any())).thenThrow(badRequestExceptionMock);

        UserDTOEditRequest userDTOEditRequest = UserDTOEditRequestBuilder.createValidUserDTOEditRequest();

        ResponseEntity<Object> responseEntity = this.userController.edit("7f4baffd-96f8-4b5a-b1de-84437519ffc2", userDTOEditRequest);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Error: Usuário não encontrado");
    }

    // DELETE /{userId}

    @Test
    public void esperoQueRetorneUmCodigoDeStatus204EOUsuarioSejaDeletado() throws BadRequestException {
        String userId = "7f4baffd-96f8-4b5a-b1de-84437519ffc2";
        BDDMockito.when(this.userService.delete(ArgumentMatchers.any())).thenReturn(userId);

        ResponseEntity<Object> responseEntity = this.userController.delete(userId);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(userId);
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus404ComUmaMensagemDeErroDeUsuarioNaoEncontrado() throws BadRequestException {
        BadRequestException badRequestExceptionMock = new BadRequestException("Error: Usuário não encontrado");
        BDDMockito.when(userService.delete(ArgumentMatchers.any())).thenThrow(badRequestExceptionMock);

        ResponseEntity<Object> responseEntity = this.userController.delete("7f4baffd-96f8-4b5a-b1de-84437519ffc2");

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Error: Usuário não encontrado");
    }
}
