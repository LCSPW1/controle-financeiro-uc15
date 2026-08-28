package com.controlefinanceiro.web.controller.dto;

import com.controlefinanceiro.core.model.Movimentacao;

public record MovimentacaoResponse(
        int id,
        String data,
        String descricao,
        String categoria,
        String tipo,
        double valor
) {
    public static MovimentacaoResponse de(Movimentacao m) {
        return new MovimentacaoResponse(
                m.getId(),
                m.getData().toString(),
                m.getDescricao(),
                m.getNomeCategoria(),
                m.getTipo().name(),
                m.getValor()
        );
    }
}
