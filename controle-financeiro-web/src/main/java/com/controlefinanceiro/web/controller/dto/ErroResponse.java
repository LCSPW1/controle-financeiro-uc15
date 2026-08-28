package com.controlefinanceiro.web.controller.dto;

/** Formato padronizado de erro devolvido pela API (ver GlobalExceptionHandler). */
public record ErroResponse(String mensagem) {
}
