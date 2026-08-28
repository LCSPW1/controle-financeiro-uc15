package com.controlefinanceiro.web.controller.dto;

/** Corpo esperado em POST /api/auth/login. */
public record LoginRequest(String email, String senha) {
}
