package com.controlefinanceiro.web.controller.dto;

import com.controlefinanceiro.core.model.Usuario;

/**
 * Representação de {@link Usuario} exposta pela API — de propósito sem o
 * campo "senha" (nem o hash deveria ser exposto ao cliente).
 */
public record UsuarioResponse(int id, String nome, String email) {

    public static UsuarioResponse de(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
