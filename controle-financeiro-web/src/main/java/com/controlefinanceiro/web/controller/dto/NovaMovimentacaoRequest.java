package com.controlefinanceiro.web.controller.dto;

public record NovaMovimentacaoRequest(
        int idUsuario,
        double valor,
        String descricao,
        String tipo,
        int idCategoria
) {
}
