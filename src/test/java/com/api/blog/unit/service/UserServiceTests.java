package com.api.blog.unit.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.api.blog.domain.exceptions.InvalidDomainDataException;
import com.api.blog.dto.UserDTO;
import com.api.blog.model.UserModel;
import com.api.blog.repositories.UserRepository;
import com.api.blog.services.UserService;
import com.api.blog.services.util.BadRequestException;
import com.api.blog.unit.util.builders.UserDTOBuilder;
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
    
        UserDTO userDTO = UserDTOBuilder.createValidUserDTO();
        UserModel response = userService.save(userDTO);

        Assertions.assertThat(response.getUserId()).isNotEqualTo(null);
        Assertions.assertThat(response.getName()).isEqualTo(userModel.getName());
        Assertions.assertThat(response.getEmail()).isEqualTo(userModel.getEmail());
        Assertions.assertThat(response.getPassword()).isEqualTo(userModel.getPassword());
    }

    @Test
    public void esperoQueRetorneUmErro() throws InvalidDomainDataException, BadRequestException {
        BDDMockito.when(userRepository.existsByEmail(ArgumentMatchers.any())).thenReturn(true);
    
        UserDTO userDTO = UserDTOBuilder.createValidUserDTO();

        Assertions.assertThatExceptionOfType(BadRequestException.class)
            .isThrownBy(() -> userService.save(userDTO))
            .withMessage("Error: Este e-mail já está cadastrado");
    }
}
