package com.api.blog.unit.controller;

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
import com.api.blog.dto.UserDTO;
import com.api.blog.model.UserModel;
import com.api.blog.services.UserService;
import com.api.blog.services.util.BadRequestException;
import com.api.blog.unit.util.builders.UserDTOBuilder;
import com.api.blog.unit.util.builders.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class UserControllerTests {
    @InjectMocks
    private UserController userController;
    
    @Mock
    private UserService userService;

    @Test
    public void esperoQueRetorneUmCodigoDeStatus201EONovoUsuarioCriado() throws InvalidDomainDataException, BadRequestException {
        UserModel userModel = UserModelBuilder.createValidUserModel();
        BDDMockito.when(userService.save(ArgumentMatchers.any())).thenReturn(userModel);

        UserDTO userDTO = UserDTOBuilder.createValidUserDTO();
        ResponseEntity<Object> responseEntity = this.userController.save(userDTO);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(userModel);
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus401ComUmaExcecaoDeInvalidDomainException() throws InvalidDomainDataException, BadRequestException {
        InvalidDomainDataException invalidDomainDataException = new InvalidDomainDataException("name: must not be blank");
        BDDMockito.when(userService.save(ArgumentMatchers.any())).thenThrow(invalidDomainDataException);
        
        UserDTO userDTO = UserDTOBuilder.createValidUserDTO();
        ResponseEntity<Object> responseEntity = this.userController.save(userDTO);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("name: must not be blank");
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus401ComUmaExcecaoBadRequest() throws InvalidDomainDataException, BadRequestException {
        BadRequestException badRequestException = new BadRequestException("Error: Este e-mail já está cadastrado");
        BDDMockito.when(userService.save(ArgumentMatchers.any())).thenThrow(badRequestException);
        
        UserDTO userDTO = UserDTOBuilder.createValidUserDTO();
        ResponseEntity<Object> responseEntity = this.userController.save(userDTO);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo("Error: Este e-mail já está cadastrado");
    }
}
