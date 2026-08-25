package com.controlefinanceiro.core.repository;

import com.controlefinanceiro.core.model.Movimentacao;
import com.controlefinanceiro.core.model.TipoMovimentacao;
import java.util.List;

public interface MovimentacaoRepository {

    void salvar(Movimentacao movimentacao);

    List<Movimentacao> listarPorUsuarioETipo(int idUsuario, TipoMovimentacao tipo);
}
