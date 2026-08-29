package com.controlefinanceiro.web.config;

import com.controlefinanceiro.core.infra.ConexaoDB;
import com.controlefinanceiro.core.infra.DatabaseConfig;
import com.controlefinanceiro.core.repository.CategoriaRepository;
import com.controlefinanceiro.core.repository.MovimentacaoRepository;
import com.controlefinanceiro.core.repository.UsuarioRepository;
import com.controlefinanceiro.core.repository.jdbc.CategoriaDAO;
import com.controlefinanceiro.core.repository.jdbc.MovimentacaoDAO;
import com.controlefinanceiro.core.repository.jdbc.UsuarioDAO;
import com.controlefinanceiro.core.security.Pbkdf2PasswordHasher;
import com.controlefinanceiro.core.security.PasswordHasher;
import com.controlefinanceiro.core.service.CategoriaService;
import com.controlefinanceiro.core.service.MovimentacaoService;
import com.controlefinanceiro.core.service.UsuarioService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryBeansConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Bean
    public DatabaseConfig databaseConfig() {
        return new DatabaseConfig(datasourceUrl, datasourceUsername, datasourcePassword);
    }

    @Bean
    public ConexaoDB conexaoDB(DatabaseConfig databaseConfig) {
        return new ConexaoDB(databaseConfig);
    }

    @Bean
    public PasswordHasher passwordHasher() {
        return new Pbkdf2PasswordHasher();
    }

    @Bean
    public UsuarioRepository usuarioRepository(ConexaoDB conexaoDB) {
        return new UsuarioDAO(conexaoDB);
    }

    @Bean
    public CategoriaRepository categoriaRepository(ConexaoDB conexaoDB) {
        return new CategoriaDAO(conexaoDB);
    }

    @Bean
    public MovimentacaoRepository movimentacaoRepository(ConexaoDB conexaoDB) {
        return new MovimentacaoDAO(conexaoDB);
    }

    @Bean
    public UsuarioService usuarioService(UsuarioRepository usuarioRepository, PasswordHasher passwordHasher) {
        return new UsuarioService(usuarioRepository, passwordHasher);
    }

    @Bean
    public CategoriaService categoriaService(CategoriaRepository categoriaRepository) {
        return new CategoriaService(categoriaRepository);
    }

    @Bean
    public MovimentacaoService movimentacaoService(MovimentacaoRepository movimentacaoRepository,
                                                     CategoriaService categoriaService) {
        return new MovimentacaoService(movimentacaoRepository, categoriaService);
    }
}
