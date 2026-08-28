package com.controlefinanceiro.web.controller.dto;

/** Corpo esperado em POST /api/movimentacoes. */
public record NovaMovimentacaoRequest(
        int idUsuario,
        double valor,
        String descricao,
        String tipo,
        int idCategoria
) {
}
