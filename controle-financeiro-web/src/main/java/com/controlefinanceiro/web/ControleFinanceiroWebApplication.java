package com.controlefinanceiro.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação web (Etapa 9).
 *
 * Ao iniciar, o Spring Boot sobe um servidor embutido (Tomcat, por
 * padrão) na porta configurada em application.properties, executa
 * schema.sql/data.sql no banco configurado, registra os beans definidos
 * em {@link com.controlefinanceiro.web.config.RepositoryBeansConfig}, e
 * passa a servir tanto a API REST (pacote controller) quanto os arquivos
 * estáticos do front-end da Etapa 8 (em src/main/resources/static).
 */
@SpringBootApplication
public class ControleFinanceiroWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControleFinanceiroWebApplication.class, args);
    }
}
