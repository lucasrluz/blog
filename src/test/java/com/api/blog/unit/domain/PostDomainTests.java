package com.api.blog.unit.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.api.blog.domain.PostDomain;
import com.api.blog.domain.exceptions.InvalidDomainDataException;

public class PostDomainTests {
    @Test
    public void esperoQueNaoRetorneUmaExcecaoDadoValoresValidos() throws InvalidDomainDataException {
        PostDomain postDomain = PostDomain.validate("foo", "foo");

        Assertions.assertThat(postDomain.getTitle()).isEqualTo("foo");
        Assertions.assertThat(postDomain.getContent()).isEqualTo("foo");
    }

    @Test
    public void esperoQueRetorneUmExcecaoDadoUmTituloInvalido() {
        Assertions.assertThatExceptionOfType(InvalidDomainDataException.class)
            .isThrownBy(() -> PostDomain.validate("", "foo"))
            .withMessage("title: must not be blank");
    }

    @Test
    public void esperoQueRetorneUmExcecaoDadoUmConteudoInvalido() {
        Assertions.assertThatExceptionOfType(InvalidDomainDataException.class)
            .isThrownBy(() -> PostDomain.validate("foo", ""))
            .withMessage("content: must not be blank");
    }
}
