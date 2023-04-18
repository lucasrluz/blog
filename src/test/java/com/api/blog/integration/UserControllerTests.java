package com.api.blog.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.api.blog.dto.UserDTOEditRequest;
import com.api.blog.dto.UserDTORequest;
import com.api.blog.model.UserModel;
import com.api.blog.repositories.UserRepository;
import com.api.blog.unit.util.builders.UserDTOEditRequestBuilder;
import com.api.blog.unit.util.builders.UserDTORequestBuilder;
import com.api.blog.unit.util.builders.UserModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();

        this.mockMvc.perform(
            post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDTORequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("name", is(userDTORequest.name)))
            .andExpect(jsonPath("email", is(userDTORequest.email)))
            .andExpect(jsonPath("password", is(userDTORequest.password)));
        
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmMensagemDeErroDadoUmNomeInvalido() throws Exception {
        UserDTORequest userDTORequest = UserDTORequestBuilder.createUserDTORequestWithInvalidName();

        this.mockMvc.perform(
            post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDTORequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("name: must not be blank")));
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmMensagemDeErroDadoUmEmailInvalido() throws Exception {
        UserDTORequest userDTORequest = UserDTORequestBuilder.createUserDTORequestWithInvalidEmail();

        this.mockMvc.perform(
            post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDTORequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("email: must be a well-formed email address")));
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComUmMensagemDeErroDadoUmaSenhaInvalida() throws Exception {
        UserDTORequest userDTORequest = UserDTORequestBuilder.createUserDTORequestWithInvalidPassword();

        this.mockMvc.perform(
            post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDTORequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("password: must not be blank")));
    }

    @Test
    public void esperoQueRetorneUmCodigoDeStatus400ComErroDeEmailJaUtilizado() throws Exception {
        UserModel userModel = UserModelBuilder.createValidUserModel();

        this.userRepository.save(userModel);
        
        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();

        this.mockMvc.perform(
            post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(userDTORequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("Error: Este e-mail já está cadastrado")));
        
        this.userRepository.deleteAll();
    }

    // GET /user/{id}

    @Test
    public void esperoQueRetorneUmUsuarioDeAcordoComOIdPassado() throws Exception {
        UserModel userModel = UserModelBuilder.createValidUserModel();

        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        UserDTORequest userDTORequest = UserDTORequestBuilder.createValidUserDTORequest();

        String url = "/user/" + saveUserModelResponse.getUserId();

        this.mockMvc
            .perform(get(url))
            .andExpect(status().isOk())
            .andExpect(jsonPath("userId", is(saveUserModelResponse.getUserId().toString())))
            .andExpect(jsonPath("name", is(userDTORequest.name)))
            .andExpect(jsonPath("email", is(userDTORequest.email)))
            .andExpect(jsonPath("password", is(userDTORequest.password)));
        
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmErroDeBadRequestPeloIdInvalido() throws Exception {
        this.mockMvc
            .perform(get("/user/7f4baffd-96f8-4b5a-b1de-84437519ffc2"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("Error: Usuário não encontrado")));
    }

    // PUT /user/{userId}

    @Test
    public void esperoQueRetorneOUsuarioEditadoPeloIdInformado() throws Exception {
        UserModel userModel = UserModelBuilder.createValidUserModel();
        
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        UserDTOEditRequest userDTOEditRequest = UserDTOEditRequestBuilder.createValidUserDTOEditRequest();

        String url = "/user/" + saveUserModelResponse.getUserId();
        
        this.mockMvc
            .perform(
                put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTOEditRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId", is(saveUserModelResponse.getUserId().toString())))
            .andExpect(jsonPath("$.newName", is(userDTOEditRequest.newName)))
            .andExpect(jsonPath("$.email", is(userDTOEditRequest.email)))
            .andExpect(jsonPath("$.password", is(userDTOEditRequest.password)));
        
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmErroDeUsuarioNaoEncontradoPeloIdInformado() throws Exception {
        UserDTOEditRequest userDTOEditRequest = UserDTOEditRequestBuilder.createValidUserDTOEditRequest();
        
        this.mockMvc
            .perform(
                put("/user/7f4baffd-96f8-4b5a-b1de-84437519ffc2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTOEditRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("Error: Usuário não encontrado")));
    }

    @Test
    public void esperoQueRetorneUmErroDadoUmaSenhaInvalida() throws Exception {
        UserModel userModel = UserModelBuilder.createValidUserModel();
        
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        UserDTOEditRequest userDTOEditRequest = UserDTOEditRequestBuilder.createUserDTOEditRequestWithInvalidPassword();

        String url = "/user/" + saveUserModelResponse.getUserId();

        this.mockMvc
            .perform(
                put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTOEditRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("Error: E-mail ou senha inválida")));
        
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneUmErroDadoUmEmailInvalido() throws Exception {
        UserModel userModel = UserModelBuilder.createValidUserModel();
        
        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        UserDTOEditRequest userDTOEditRequest = UserDTOEditRequestBuilder.createUserDTOEditRequestWithInvalidEmail();

        String url = "/user/" + saveUserModelResponse.getUserId();

        this.mockMvc
            .perform(
                put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTOEditRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("Error: E-mail ou senha inválida")));
        
        this.userRepository.deleteAll();
    }

    // DELETE /user/{userId}

    @Test
    public void esperoQueOUsuarioSejaDeletadoPeloIdInformado() throws Exception {
        UserModel userModel = UserModelBuilder.createValidUserModel();

        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        String url = "/user/" + saveUserModelResponse.getUserId();

        this.mockMvc
            .perform(delete(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(saveUserModelResponse.getUserId().toString())));
        
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetoneCodigoDeStatus404ComUmaMensagemDeUsuarioNaoEncontrado() throws Exception {
        this.mockMvc
            .perform(delete("/user/7f4baffd-96f8-4b5a-b1de-84437519ffc2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is("Error: Usuário não encontrado")));
    }
}
