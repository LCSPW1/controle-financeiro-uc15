package com.controlefinanceiro.core.service;

import com.controlefinanceiro.core.exception.AutenticacaoException;
import com.controlefinanceiro.core.exception.ValidacaoException;
import com.controlefinanceiro.core.model.Usuario;
import com.controlefinanceiro.core.repository.UsuarioRepository;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario autenticar(String email, String senha) {
        if (isNuloOuVazio(email) || isNuloOuVazio(senha)) {
            throw new ValidacaoException("Preencha email e senha.");
        }

        Usuario usuario = usuarioRepository.autenticar(email.trim(), senha.trim());
        if (usuario == null) {
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
        usuario.setSenha(senha);

        usuarioRepository.salvar(usuario);
    }

    private boolean isNuloOuVazio(String valor) {
        return valor == null || valor.trim().isEmpty();
    }
}
