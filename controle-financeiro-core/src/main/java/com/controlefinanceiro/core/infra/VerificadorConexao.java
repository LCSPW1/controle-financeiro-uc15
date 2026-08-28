package com.controlefinanceiro.core.infra;

import java.sql.Connection;

public final class VerificadorConexao {

    private final ConexaoDB conexaoDB;

    public VerificadorConexao(ConexaoDB conexaoDB) {
        this.conexaoDB = conexaoDB;
    }

    public boolean conexaoDisponivel() {
        try (Connection conn = conexaoDB.getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
