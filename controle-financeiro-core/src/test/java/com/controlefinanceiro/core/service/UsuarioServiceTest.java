package com.controlefinanceiro.core.service;

import com.controlefinanceiro.core.exception.AutenticacaoException;
import com.controlefinanceiro.core.exception.ValidacaoException;
import com.controlefinanceiro.core.model.Usuario;
import com.controlefinanceiro.core.repository.memory.InMemoryUsuarioRepository;
import com.controlefinanceiro.core.security.Pbkdf2PasswordHasher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários das regras de negócio de UsuarioService (cadastro e
 * login), usando a implementação em memória do repositório para dispensar
 * dependência de banco de dados real, conforme permitido pelo enunciado.
 *
 * Desde a Etapa 9, UsuarioService também depende de um PasswordHasher
 * (aqui, Pbkdf2PasswordHasher — ver justificativa em
 * com.controlefinanceiro.core.security.PasswordHasher).
 */
class UsuarioServiceTest {

    private UsuarioService usuarioService;
    private InMemoryUsuarioRepository usuarioRepository;

    @BeforeEach
    void configurar() {
        usuarioRepository = new InMemoryUsuarioRepository();
        usuarioService = new UsuarioService(usuarioRepository, new Pbkdf2PasswordHasher());
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

    /**
     * Regressão da correção de segurança da Etapa 9: a senha nunca deve
     * ser persistida em texto puro no repositório, mesmo que o login com
     * a senha original continue funcionando normalmente.
     */
    @Test
    void naoDeveArmazenarSenhaEmTextoPuro() {
        usuarioService.cadastrar("Maria Silva", "maria@teste.com", "123456");

        Usuario usuarioPersistido = usuarioRepository.buscarPorEmail("maria@teste.com").orElseThrow();

        assertNotEquals("123456", usuarioPersistido.getSenha());
    }
}
