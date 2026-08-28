package com.controlefinanceiro.core.service;

import com.controlefinanceiro.core.exception.ValidacaoException;
import com.controlefinanceiro.core.model.Categoria;
import com.controlefinanceiro.core.model.Movimentacao;
import com.controlefinanceiro.core.model.ResumoFinanceiro;
import com.controlefinanceiro.core.model.TipoMovimentacao;
import com.controlefinanceiro.core.model.Usuario;
import com.controlefinanceiro.core.repository.MovimentacaoRepository;

import java.time.LocalDate;

public class MovimentacaoService {

    private final MovimentacaoRepository movimentacaoRepository;
    private final CategoriaService categoriaService;

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository,
                                CategoriaService categoriaService) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.categoriaService = categoriaService;
    }

    public Movimentacao registrarMovimentacao(Usuario usuarioLogado, double valor, String descricao,
                                               TipoMovimentacao tipo, int idCategoria) {
        if (valor <= 0) {
            throw new ValidacaoException("Informe um valor numérico válido maior que zero!");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new ValidacaoException("Informe a descrição!");
        }
        if (tipo == null) {
            throw new ValidacaoException("Selecione Entrada ou Saída!");
        }

        Categoria categoria = categoriaService.buscarCategoriaValida(idCategoria);

        Movimentacao m = new Movimentacao();
        m.setValor(valor);
        m.setDescricao(descricao.trim());
        m.setData(LocalDate.now());
        m.setIdUsuario(usuarioLogado.getId());
        m.setIdCategoria(categoria.getId());
        m.setNomeCategoria(categoria.getNome());
        m.setTipo(tipo);

        movimentacaoRepository.salvar(m);
        return m;
    }

    public java.util.List<Movimentacao> listarMovimentacoes(int idUsuario) {
        return movimentacaoRepository.listarPorUsuario(idUsuario);
    }

    public ResumoFinanceiro gerarResumo(int idUsuario) {
        double totalEntradas = movimentacaoRepository
                .listarPorUsuarioETipo(idUsuario, TipoMovimentacao.ENTRADA)
                .stream()
                .mapToDouble(Movimentacao::getValor)
                .sum();

        double totalSaidas = movimentacaoRepository
                .listarPorUsuarioETipo(idUsuario, TipoMovimentacao.SAIDA)
                .stream()
                .mapToDouble(Movimentacao::getValor)
                .sum();

        return new ResumoFinanceiro(totalEntradas, totalSaidas);
    }
}
