package com.controlefinanceiro.web.controller.dto;

import com.controlefinanceiro.core.model.Usuario;

public record UsuarioResponse(int id, String nome, String email) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
