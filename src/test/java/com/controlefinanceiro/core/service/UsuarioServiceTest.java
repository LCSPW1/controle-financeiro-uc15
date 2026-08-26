package com.controlefinanceiro.core.service;

import com.controlefinanceiro.core.exception.AutenticacaoException;
import com.controlefinanceiro.core.exception.ValidacaoException;
import com.controlefinanceiro.core.model.Usuario;
import com.controlefinanceiro.core.repository.memory.InMemoryUsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários das regras de negócio de UsuarioService (cadastro e
 * login), usando a implementação em memória do repositório para dispensar
 * dependência de banco de dados real, conforme permitido pelo enunciado.
 */
class UsuarioServiceTest {

    private UsuarioService usuarioService;
    private InMemoryUsuarioRepository usuarioRepository;

    @BeforeEach
    void configurar() {
        usuarioRepository = new InMemoryUsuarioRepository();
        usuarioService = new UsuarioService(usuarioRepository);
    }

    @Test
    void deveCadastrarUsuarioComDadosValidos() {
        usuarioService.cadastrar("Maria Silva", "maria@teste.com", "123456");

        assertTrue(usuarioRepository.existeEmail("maria@teste.com"));
    }

    @Test
    void deveLancarExcecaoAoCadastrarComEmailDuplicado() {
        usuarioService.cadastrar("Maria Silva", "maria@teste.com", "123456");

        assertThrows(ValidacaoException.class, () ->
            usuarioService.cadastrar("Maria Duplicada", "maria@teste.com", "outrasenha")
        );
    }

    @Test
    void deveLancarExcecaoAoCadastrarComCampoVazio() {
        assertThrows(ValidacaoException.class, () ->
            usuarioService.cadastrar("", "maria@teste.com", "123456")
        );
    }

    @Test
    void deveLancarExcecaoAoCadastrarComEmailSemArroba() {
        assertThrows(ValidacaoException.class, () ->
            usuarioService.cadastrar("Maria Silva", "email-invalido", "123456")
        );
    }

    @Test
    void deveAutenticarComCredenciaisCorretas() {
        usuarioService.cadastrar("Maria Silva", "maria@teste.com", "123456");

        Usuario usuario = usuarioService.autenticar("maria@teste.com", "123456");

        assertEquals("Maria Silva", usuario.getNome());
    }

    @Test
    void deveLancarExcecaoAoAutenticarComSenhaIncorreta() {
        usuarioService.cadastrar("Maria Silva", "maria@teste.com", "123456");

        assertThrows(AutenticacaoException.class, () ->
            usuarioService.autenticar("maria@teste.com", "senhaErrada")
        );
    }
}
