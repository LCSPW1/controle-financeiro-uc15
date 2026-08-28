package com.controlefinanceiro.core.infra;

import com.controlefinanceiro.core.exception.PersistenciaException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexaoDB {

    private final DatabaseConfig config;

    public ConexaoDB(DatabaseConfig config) {
        this.config = config;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(config.getUrl(), config.getUsuario(), config.getSenha());
        } catch (SQLException e) {
            throw new PersistenciaException("Falha ao conectar ao banco de dados.", e);
        }
    }
}
