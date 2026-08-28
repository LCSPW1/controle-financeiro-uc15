package com.controlefinanceiro.core.security;

/**
 * Abstrai o mecanismo de hash de senha.
 *
 * Adicionada na Etapa 9: ao integrar o UsuarioService com um back-end web
 * real, ficou evidente um problema que passou despercebido nas Etapas 6/7
 * (onde tudo rodava localmente, sem exposição pela rede): as senhas eram
 * armazenadas e comparadas em texto puro, tanto em memória quanto no banco
 * de dados. Isso é aceitável para os testes automatizados das etapas
 * anteriores, mas inadequado para uma aplicação web real, onde o banco de
 * dados pode vazar ou ser acessado por terceiros.
 *
 * Esta interface segue o mesmo padrão de Dependency Inversion Principle já
 * usado com os repositórios: UsuarioService depende apenas desta
 * abstração, não de um algoritmo de hash específico.
 */
public interface PasswordHasher {

    /** Gera um hash a partir da senha em texto puro, pronto para ser armazenado. */
    String hash(String senhaPlana);

    /** Verifica se a senha em texto puro corresponde ao hash armazenado. */
    boolean matches(String senhaPlana, String hashArmazenado);
}
