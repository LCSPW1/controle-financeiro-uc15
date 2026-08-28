package com.controlefinanceiro.web.controller.dto;

/** Corpo esperado em POST /api/usuarios. */
public record CadastroUsuarioRequest(String nome, String email, String senha) {
}
