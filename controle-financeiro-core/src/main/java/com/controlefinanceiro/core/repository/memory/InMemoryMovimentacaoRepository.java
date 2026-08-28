package com.controlefinanceiro.core.repository.memory;

import com.controlefinanceiro.core.model.Movimentacao;
import com.controlefinanceiro.core.model.TipoMovimentacao;
import com.controlefinanceiro.core.repository.MovimentacaoRepository;

import java.util.ArrayList;
import java.util.Comparator;
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

    @Override
    public List<Movimentacao> listarPorUsuario(int idUsuario) {
        List<Movimentacao> resultado = new ArrayList<>();
        for (Movimentacao m : movimentacoes) {
            if (m.getIdUsuario() == idUsuario) {
                resultado.add(m);
            }
        }
        resultado.sort(Comparator.comparing(Movimentacao::getData).reversed()
                .thenComparing(Comparator.comparingInt(Movimentacao::getId).reversed()));
        return resultado;
    }
}
