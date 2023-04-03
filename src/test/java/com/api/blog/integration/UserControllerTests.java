package com.api.blog.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.api.blog.dto.UserDTO;
import com.api.blog.model.UserModel;
import com.api.blog.repositories.UserRepository;
import com.api.blog.unit.util.builders.UserDTOBuilder;
import com.api.blog.unit.util.builders.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    public static String asJsonString(final Object obj) {
        try {
          return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus201EONovoUsuarioSalvo() throws Exception {
        UserDTO userDTO = UserDTOBuilder.createValidUserDTO();

        this.mockMvc.perform(
            post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("name", is(userDTO.name)))
            .andExpect(jsonPath("email", is(userDTO.email)))
            .andExpect(jsonPath("password", is(userDTO.password)));
        
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus401ComUmMensagemDeErroDadoUmNomeInvalido() throws Exception {
        UserDTO userDTO = UserDTOBuilder.createUserDTOWithInvalidName();

        this.mockMvc.perform(
            post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("name: must not be blank")));
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus401ComUmMensagemDeErroDadoUmEmailInvalido() throws Exception {
        UserDTO userDTO = UserDTOBuilder.createUserDTOWithInvalidEmail();

        this.mockMvc.perform(
            post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("email: must be a well-formed email address")));
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus401ComUmMensagemDeErroDadoUmaSenhaInvalida() throws Exception {
        UserDTO userDTO = UserDTOBuilder.createUserDTOWithInvalidPassword();

        this.mockMvc.perform(
            post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("password: must not be blank")));
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus401ComErroDeEmailJaUtilizado() throws Exception {
        UserModel userModel = UserModelBuilder.createValidUserModel();

        this.userRepository.save(userModel);
        
        UserDTO userDTO = UserDTOBuilder.createValidUserDTO();

        this.mockMvc.perform(
            post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("Error: Este e-mail já está cadastrado")));
        
        this.userRepository.deleteAll();
    }
}
