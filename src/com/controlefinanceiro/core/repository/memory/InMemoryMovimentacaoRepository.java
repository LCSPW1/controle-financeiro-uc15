package com.controlefinanceiro.core.repository.memory;

import com.controlefinanceiro.core.model.Movimentacao;
import com.controlefinanceiro.core.model.TipoMovimentacao;
import com.controlefinanceiro.core.repository.MovimentacaoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMovimentacaoRepository implements MovimentacaoRepository {

    private final List<Movimentacao> movimentacoes = new ArrayList<>();
    private final AtomicInteger proximoId = new AtomicInteger(1);

    @Override
    public void salvar(Movimentacao movimentacao) {
        movimentacao.setId(proximoId.getAndIncrement());
        movimentacoes.add(movimentacao);
    }

    @Override
    public List<Movimentacao> listarPorUsuarioETipo(int idUsuario, TipoMovimentacao tipo) {
        List<Movimentacao> resultado = new ArrayList<>();
        for (Movimentacao m : movimentacoes) {
            if (m.getIdUsuario() == idUsuario && m.getTipo() == tipo) {
                resultado.add(m);
            }
        }
        return resultado;
    }
}
