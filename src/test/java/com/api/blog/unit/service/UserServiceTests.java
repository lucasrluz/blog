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
import com.api.blog.dto.UserDTOEditRequest;
import com.api.blog.dto.UserDTOEditResponse;
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

        UserModel userModelMock = UserModelBuilder.createValidUserModel();
        BDDMockito.when(userRepository.save(ArgumentMatchers.any())).thenReturn(userModelMock);
    
        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();
        UserDTOResponse response = userService.save(userDTORequest);

        Assertions.assertThat(response.userId).isEqualTo(userModelMock.getUserId());
        Assertions.assertThat(response.name).isEqualTo(userDTORequest.name);
        Assertions.assertThat(response.email).isEqualTo(userDTORequest.email);
        Assertions.assertThat(response.password).isEqualTo(userDTORequest.password);
    }

    @Test
    public void esperoQueRetorneUmErroDadoUmEmailJaCadastrado() throws InvalidDomainDataException, BadRequestException {
        BDDMockito.when(userRepository.existsByEmail(ArgumentMatchers.any())).thenReturn(true);
    
        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> userService.save(userDTORequest))
            .withMessage("Error: Este e-mail já está cadastrado");
    }

    // userService.findById

    @Test
    public void esperoQueRetorneUmUsuarioPeloIdInformado() throws BadRequestException {
        UserModel userModelMock = UserModelBuilder.createValidUserModel();
        Optional<UserModel> userOptionalMock = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userOptionalMock);

        String uuid = new UUID(0, 0).toString();

        UserDTOResponse response = userService.getByUserId(uuid);

        Assertions.assertThat(response.userId).isEqualTo(userModelMock.getUserId());
        Assertions.assertThat(response.name).isEqualTo(userModelMock.getName());
        Assertions.assertThat(response.email).isEqualTo(userModelMock.getEmail());
        Assertions.assertThat(response.password).isEqualTo(userModelMock.getPassword());
    }

    @Test
    public void esperoQueRetorneUmBadRequestExceptionPorNaoTerEncontradoOUsuarioPeloIdInformado() throws BadRequestException {
        Optional<UserModel> userOptionalMock = Optional.empty();
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userOptionalMock);

        String uuid = new UUID(0, 0).toString();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> userService.getByUserId(uuid))
            .withMessage("Error: Usuário não encontrado");
    }

    // userService.edit

    @Test
    public void esperoQueEditeONomeDoUsuarioPeloIdInformado() throws BadRequestException {
        UserModel userModelMockFindById = UserModelBuilder.createValidUserModel();
        Optional<UserModel> userOptionalMock = Optional.of(userModelMockFindById);
        
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userOptionalMock);

        UserModel userModelMockInSave = UserModelBuilder.createValidUserModel();
        userModelMockInSave.setName("new name");

        BDDMockito.when(this.userRepository.save(ArgumentMatchers.any())).thenReturn(userModelMockInSave);

        UserDTOEditRequest userDTOEditRequest = new UserDTOEditRequest(
            userModelMockFindById.getUserId().toString(),
            "new name",
            userModelMockFindById.getEmail(),
            userModelMockFindById.getPassword()
        );

        UserDTOEditResponse response = userService.edit(userDTOEditRequest);

        Assertions.assertThat(response.userId).isEqualTo(userDTOEditRequest.userId);
        Assertions.assertThat(response.newName).isEqualTo(userDTOEditRequest.newName);
        Assertions.assertThat(response.email).isEqualTo(userDTOEditRequest.email);
        Assertions.assertThat(response.password).isEqualTo(userDTOEditRequest.password);
    }

    @Test
    public void esperoQueRetorneUmErroDeUsuarioNaoEncontradoDadoUmIdInvalido() throws BadRequestException {
        UserModel userModelMockFindById = UserModelBuilder.createValidUserModel();
        Optional<UserModel> userOptionalMock = Optional.empty();
        
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userOptionalMock);

        UserDTOEditRequest userDTOEditRequest = new UserDTOEditRequest(
            userModelMockFindById.getUserId().toString(),
            "new name",
            userModelMockFindById.getEmail(),
            userModelMockFindById.getPassword()
        );

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> userService.edit(userDTOEditRequest))
            .withMessage("Error: Usuário não encontrado");
    }

    @Test
    public void esperoQueRetorneUmErroDeEmailOuSenhaInvalidaDadoUmaSenhaInvalida() throws BadRequestException {
        UserModel userModelMockFindById = UserModelBuilder.createValidUserModel();
        Optional<UserModel> userOptionalMock = Optional.of(userModelMockFindById);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userOptionalMock);

        UserDTOEditRequest userDTOEditRequest = new UserDTOEditRequest(
            userModelMockFindById.getUserId().toString(),
            "new name",
            userModelMockFindById.getEmail(),
            "invalidpassword"
        );

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> userService.edit(userDTOEditRequest))
            .withMessage("Error: E-mail ou senha inválida");
    }

    @Test
    public void esperoQueRetorneUmErroDeEmailOuSenhaInvalidaDadoUmEmailInvalido() throws BadRequestException {
        UserModel userModelMock = UserModelBuilder.createValidUserModel();
        Optional<UserModel> userOptional = Optional.of(userModelMock);
        BDDMockito.when(this.userRepository.findById(ArgumentMatchers.any())).thenReturn(userOptional);

        UserDTOEditRequest userDTOEditRequest = new UserDTOEditRequest(
            userModelMock.getUserId().toString(),
            "new name",
            "invalidemail@gmail.com",
            userModelMock.getPassword()
        );

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> userService.edit(userDTOEditRequest))
            .withMessage("Error: E-mail ou senha inválida");
    }
}
