package com.controlefinanceiro.core.infra;

import com.controlefinanceiro.core.exception.PersistenciaException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DatabaseConfig {

    private static final String ARQUIVO_CONFIG = "db.properties";

    private final String url;
    private final String usuario;
    private final String senha;

    public DatabaseConfig(String url, String usuario, String senha) {
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
    }

    public static DatabaseConfig carregarDoClasspath() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream(ARQUIVO_CONFIG)) {

            if (input == null) {
                return valoresPadraoDesenvolvimento();
            }
            props.load(input);
        } catch (IOException e) {
            throw new PersistenciaException("Não foi possível ler o arquivo " + ARQUIVO_CONFIG, e);
        }

        String url = props.getProperty("db.url");
        String usuario = props.getProperty("db.user");
        String senha = props.getProperty("db.password");

        if (url == null || usuario == null || senha == null) {
            throw new PersistenciaException(
                    "Arquivo " + ARQUIVO_CONFIG + " incompleto. São obrigatórias as chaves "
                            + "db.url, db.user e db.password.");
        }
        return new DatabaseConfig(url, usuario, senha);
    }

    private static DatabaseConfig valoresPadraoDesenvolvimento() {
        return new DatabaseConfig(
                "jdbc:mysql://localhost:3306/controle_financeiro?useSSL=false&serverTimezone=UTC",
                "root",
                "");
    }

    public String getUrl() {
        return url;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }
}
