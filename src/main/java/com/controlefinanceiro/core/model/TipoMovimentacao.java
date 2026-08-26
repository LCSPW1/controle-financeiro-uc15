package com.controlefinanceiro.core.model;

public enum TipoMovimentacao {

    SAIDA(0),
    ENTRADA(1);

    private final int codigo;

    TipoMovimentacao(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoMovimentacao porCodigo(int codigo) {
        for (TipoMovimentacao tipo : values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código de tipo de movimentação inválido: " + codigo);
    }
}
