package com.controlefinanceiro.core.repository;

import com.controlefinanceiro.core.model.Movimentacao;
import com.controlefinanceiro.core.model.TipoMovimentacao;
import java.util.List;

public interface MovimentacaoRepository {

    void salvar(Movimentacao movimentacao);

    List<Movimentacao> listarPorUsuarioETipo(int idUsuario, TipoMovimentacao tipo);

    /**
     * Lista todas as movimentações (entradas e saídas) de um usuário,
     * ordenadas da mais recente para a mais antiga.
     *
     * Adicionado na Etapa 9: nas Etapas 6/7, MovimentacaoService só
     * precisava calcular totais (gerarResumo), nunca listar os lançamentos
     * individualmente — o método main() de testes não exercitava essa
     * necessidade. Ao integrar com o front-end da Etapa 8, ficou evidente
     * que o dashboard também precisa exibir a lista de lançamentos, não
     * só o resumo agregado.
     */
    List<Movimentacao> listarPorUsuario(int idUsuario);
}
