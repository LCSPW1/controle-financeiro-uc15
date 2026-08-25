package com.controlefinanceiro.core.repository;

import com.controlefinanceiro.core.model.Usuario;

public interface UsuarioRepository {

    Usuario autenticar(String email, String senha);

    void salvar(Usuario usuario);

    boolean existeEmail(String email);
}
