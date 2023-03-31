package com.api.blog.unit.domain;

import org.junit.jupiter.api.Test;

import com.api.blog.domain.UserDomain;
import com.api.blog.domain.exceptions.InvalidDomainDataException;

import org.assertj.core.api.Assertions;;

public class UserDomainTests {
    @Test
    public void esperoQueNaoRetoneUmaExcecaoDadoValoresValidos() throws InvalidDomainDataException {
        UserDomain userDomain = UserDomain.validate("name test", "emailtest@gmail.com", "123");

        Assertions.assertThat(userDomain.getName()).isEqualTo("name test");
        Assertions.assertThat(userDomain.getEmail()).isEqualTo("emailtest@gmail.com");
        Assertions.assertThat(userDomain.getPassword()).isEqualTo("123");
    }
    
    @Test
    public void esperoQueRetorneUmaExcecaoDadoUmNomeInvalido() {
        Assertions.assertThatExceptionOfType(InvalidDomainDataException.class)
            .isThrownBy(() -> UserDomain.validate("", "emailtest@gmail.com", "123"))
            .withMessage("name: must not be blank");
    }
    
    @Test
    public void esperoQueRetorneUmaExcecaoDadoUmFormatoDeEmailInvalido() {
        Assertions.assertThatExceptionOfType(InvalidDomainDataException.class)
            .isThrownBy(() -> UserDomain.validate("name test", "@gmail.com", "123"))
            .withMessage("email: must be a well-formed email address");
    }

    @Test
    public void esperoQueRetorneUmaExcecaoDadoEmailVazioInvalido() {

        Assertions.assertThatExceptionOfType(InvalidDomainDataException.class)
            .isThrownBy(() -> UserDomain.validate("name test", "", "123"))
            .withMessage("email: must not be blank");
    }
}
