package com.controlefinanceiro.core.repository;

import com.controlefinanceiro.core.model.Usuario;
import java.util.Optional;

/**
 * Contrato de persistência para {@link Usuario}.
 *
 * Alterado na Etapa 9: o método antigo autenticar(email, senha), que
 * delegava a verificação de senha para uma cláusula SQL "WHERE senha = ?",
 * foi substituído por buscarPorEmail(email). Isso foi necessário porque,
 * com a introdução de hash de senha (PasswordHasher), não é mais possível
 * comparar a senha diretamente em uma consulta SQL — o hash precisa ser
 * verificado em código Java (ver UsuarioService.autenticar). Esta mudança
 * também é uma melhoria de Single Responsibility Principle: o repositório
 * volta a ter a única responsabilidade de buscar dados, e a decisão sobre
 * se uma credencial é válida passa a ser inteiramente do serviço.
 */
public interface UsuarioRepository {

    Optional<Usuario> buscarPorEmail(String email);

    void salvar(Usuario usuario);

    boolean existeEmail(String email);
}
