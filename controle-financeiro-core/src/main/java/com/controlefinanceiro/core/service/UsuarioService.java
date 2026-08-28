package com.controlefinanceiro.core.service;

import com.controlefinanceiro.core.exception.AutenticacaoException;
import com.controlefinanceiro.core.exception.ValidacaoException;
import com.controlefinanceiro.core.model.Usuario;
import com.controlefinanceiro.core.repository.UsuarioRepository;
import com.controlefinanceiro.core.security.PasswordHasher;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordHasher passwordHasher;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordHasher passwordHasher) {
        this.usuarioRepository = usuarioRepository;
        this.passwordHasher = passwordHasher;
    }

    public Usuario autenticar(String email, String senha) {
        if (isNuloOuVazio(email) || isNuloOuVazio(senha)) {
            throw new ValidacaoException("Preencha email e senha.");
        }

        Usuario usuario = usuarioRepository.buscarPorEmail(email.trim())
                .orElseThrow(() -> new AutenticacaoException("E-mail ou senha inválidos."));

        if (!passwordHasher.matches(senha.trim(), usuario.getSenha())) {
            throw new AutenticacaoException("E-mail ou senha inválidos.");
        }

        return usuario;
    }

    public void cadastrar(String nome, String email, String senha) {
        if (isNuloOuVazio(nome) || isNuloOuVazio(email) || isNuloOuVazio(senha)) {
            throw new ValidacaoException("Preencha todos os campos!");
        }
        if (!email.contains("@")) {
            throw new ValidacaoException("Informe um e-mail válido.");
        }
        if (usuarioRepository.existeEmail(email.trim())) {
            throw new ValidacaoException("Já existe um usuário cadastrado com este e-mail.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(nome.trim());
        usuario.setEmail(email.trim());
        usuario.setSenha(passwordHasher.hash(senha));

        usuarioRepository.salvar(usuario);
    }

    private boolean isNuloOuVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
