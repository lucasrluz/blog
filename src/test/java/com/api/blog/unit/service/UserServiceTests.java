package com.api.blog.unit.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.blog.domain.exceptions.InvalidDomainDataException;
import com.api.blog.dto.UserDTORequest;
import com.api.blog.dto.UserDTOResponse;
import com.api.blog.model.UserModel;
import com.api.blog.repositories.UserRepository;
import com.api.blog.services.UserService;
import com.api.blog.services.util.BadRequestException;
import com.api.blog.unit.util.builders.UserDTORequestBuilder;
import com.api.blog.unit.util.builders.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void esperoQueRetorneOUsuarioSalvo() throws InvalidDomainDataException, BadRequestException {
        BDDMockito.when(userRepository.existsByEmail(ArgumentMatchers.any())).thenReturn(false);

        UserModel userModel = UserModelBuilder.createValidUserModel();
        BDDMockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(userModel);
    
        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();
        UserDTOResponse response = userService.save(userDTORequest);

        Assertions.assertThat(response.userId).isNotEqualTo(null);
        Assertions.assertThat(response.name).isEqualTo(userModel.getName());
        Assertions.assertThat(response.email).isEqualTo(userModel.getEmail());
        Assertions.assertThat(response.password).isEqualTo(userModel.getPassword());
    }

    @Test
    public void esperoQueRetorneUmErro() throws InvalidDomainDataException, BadRequestException {
        BDDMockito.when(userRepository.existsByEmail(ArgumentMatchers.any())).thenReturn(true);
    
        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> userService.save(userDTORequest))
            .withMessage("Error: Este e-mail já está cadastrado");
    }

    // userService.findById

    @Test
    public void esperoQueRetorneUmUsuarioPeloIdInformado() throws BadRequestException {
        UserModel userModel = UserModelBuilder.createValidUserModel();
        Optional<UserModel> userOptional = Optional.of(userModel);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userOptional);

        UserDTOResponse response = userService.getByUserId(userModel.getUserId().toString());

        Assertions.assertThat(response.userId).isNotEqualTo(null);
        Assertions.assertThat(response.name).isEqualTo(userModel.getName());
        Assertions.assertThat(response.email).isEqualTo(userModel.getEmail());
        Assertions.assertThat(response.password).isEqualTo(userModel.getPassword());
    }

    @Test
    public void esperoQueRetorneUmBadRequestExceptionPorNaoTerEncontradoOUsuario() throws BadRequestException {
        UserModel userModel = UserModelBuilder.createValidUserModel();
        Optional<UserModel> userOptional = Optional.empty();
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userOptional);

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> userService.getByUserId(userModel.getUserId().toString()))
            .withMessage("Error: Usuário não encontrado");
    }
}
