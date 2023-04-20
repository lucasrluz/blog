package com.api.blog.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.is;

import com.api.blog.dto.postDTO.PostDTORequest;
import com.api.blog.model.UserModel;
import com.api.blog.repositories.PostRepository;
import com.api.blog.repositories.UserRepository;
import com.api.blog.unit.util.builders.PostDTORequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

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
    public void esperoQueRetorneUmCodigoDeStatus201EOIdDoNovoPostCriado() throws Exception {
        UserModel userModel = this.userRepository.save(new UserModel("foo", "foo@gmail.com", "foo"));

        PostDTORequest postDTORequest = PostDTORequestBuilder.createValidPostDTORequest();

        String url = "/post/" + userModel.getUserId().toString();

        this.mockMvc.perform(
            post(url)
            .contentType("application/json")
            .content(asJsonString(postDTORequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", not("")));

        this.postRepository.deleteAll();
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneRetorneUmCodigoDeStatus400EUmErroDeTituloInvalido() throws Exception {
        UserModel userModel = this.userRepository.save(new UserModel("foo", "foo@gmail.com", "foo"));
        
        PostDTORequest postDTORequest = PostDTORequestBuilder.createPostDTORequestWithInvalidTitle();

        String url = "/post/" + userModel.getUserId().toString();

        this.mockMvc.perform(
            post(url)
            .contentType("application/json")
            .content(asJsonString(postDTORequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("title: must not be blank")));
        
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneRetorneUmCodigoDeStatus400EUmErroDeConteudoInvalido() throws Exception {
        UserModel userModel = this.userRepository.save(new UserModel("foo", "foo@gmail.com", "foo"));
        
        PostDTORequest postDTORequest = PostDTORequestBuilder.createPostDTORequestWithInvalidContent();

        String url = "/post/" + userModel.getUserId().toString();

        this.mockMvc.perform(
            post(url)
            .contentType("application/json")
            .content(asJsonString(postDTORequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$", is("content: must not be blank")));
        
        this.userRepository.deleteAll();
    }

    @Test
    public void esperoQueRetorneRetorneUmCodigoDeStatus404EUmErroDeUsuarioNaoEncontrado() throws Exception {        
        PostDTORequest postDTORequest = PostDTORequestBuilder.createValidPostDTORequest();

        String url = "/post/" + UUID.randomUUID().toString();

        this.mockMvc.perform(
            post(url)
            .contentType("application/json")
            .content(asJsonString(postDTORequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$", is("Error: Usuário não encontrado")));        
    }
}
