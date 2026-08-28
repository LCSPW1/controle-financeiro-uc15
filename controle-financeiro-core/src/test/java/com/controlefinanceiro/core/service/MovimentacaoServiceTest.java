package com.controlefinanceiro.core.service;

import com.controlefinanceiro.core.exception.ValidacaoException;
import com.controlefinanceiro.core.model.Categoria;
import com.controlefinanceiro.core.model.ResumoFinanceiro;
import com.controlefinanceiro.core.model.TipoMovimentacao;
import com.controlefinanceiro.core.model.Usuario;
import com.controlefinanceiro.core.repository.memory.InMemoryCategoriaRepository;
import com.controlefinanceiro.core.repository.memory.InMemoryMovimentacaoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários das regras de negócio de MovimentacaoService.
 *
 * De acordo com o enunciado da Etapa 7, funcionalidades com acesso real ao
 * banco de dados foram dispensadas deste teste: em vez da implementação
 * JDBC (MovimentacaoDAO), é usada a implementação em memória
 * (InMemoryMovimentacaoRepository), já criada na Etapa 6 para viabilizar
 * testes sem dependência de MySQL. Isso é possível graças ao Dependency
 * Inversion Principle aplicado na etapa anterior: o serviço depende apenas
 * da interface MovimentacaoRepository.
 */
class MovimentacaoServiceTest {

    private MovimentacaoService movimentacaoService;
    private Usuario usuario;

    @BeforeEach
    void configurar() {
        InMemoryMovimentacaoRepository movimentacaoRepository = new InMemoryMovimentacaoRepository();
        InMemoryCategoriaRepository categoriaRepository = new InMemoryCategoriaRepository();
        categoriaRepository.adicionar(new Categoria(1, "Alimentação"));
        categoriaRepository.adicionar(new Categoria(2, "Salário"));

        CategoriaService categoriaService = new CategoriaService(categoriaRepository);
        movimentacaoService = new MovimentacaoService(movimentacaoRepository, categoriaService);

        usuario = new Usuario(1, "Maria Silva", "maria@teste.com", "123456");
    }

    @Test
    void deveRegistrarEntradaESaidaECalcularSaldoCorretamente() {
        movimentacaoService.registrarMovimentacao(usuario, 3000.0, "Salário do mês", TipoMovimentacao.ENTRADA, 2);
        movimentacaoService.registrarMovimentacao(usuario, 450.50, "Supermercado", TipoMovimentacao.SAIDA, 1);
        movimentacaoService.registrarMovimentacao(usuario, 120.0, "Restaurante", TipoMovimentacao.SAIDA, 1);

        ResumoFinanceiro resumo = movimentacaoService.gerarResumo(usuario.getId());

        assertEquals(3000.0, resumo.getTotalEntradas(), 0.001);
        assertEquals(570.50, resumo.getTotalSaidas(), 0.001);
        assertEquals(2429.50, resumo.getSaldo(), 0.001);
    }

    @Test
    void deveLancarExcecaoQuandoValorForMenorOuIgualAZero() {
        assertThrows(ValidacaoException.class, () ->
            movimentacaoService.registrarMovimentacao(usuario, 0.0, "Valor inválido", TipoMovimentacao.SAIDA, 1)
        );
    }

    @Test
    void deveLancarExcecaoQuandoValorForNegativo() {
        assertThrows(ValidacaoException.class, () ->
            movimentacaoService.registrarMovimentacao(usuario, -50.0, "Valor negativo", TipoMovimentacao.SAIDA, 1)
        );
    }

    @Test
    void deveLancarExcecaoQuandoDescricaoForVazia() {
        assertThrows(ValidacaoException.class, () ->
            movimentacaoService.registrarMovimentacao(usuario, 100.0, "   ", TipoMovimentacao.ENTRADA, 1)
        );
    }

    @Test
    void deveLancarExcecaoQuandoCategoriaForInvalida() {
        assertThrows(ValidacaoException.class, () ->
            movimentacaoService.registrarMovimentacao(usuario, 100.0, "Categoria inexistente", TipoMovimentacao.SAIDA, 999)
        );
    }

    @Test
    void resumoNaoDeveIncluirMovimentacoesDeOutroUsuario() {
        Usuario outroUsuario = new Usuario(2, "João Souza", "joao@teste.com", "abcdef");

        movimentacaoService.registrarMovimentacao(usuario, 3000.0, "Salário de Maria", TipoMovimentacao.ENTRADA, 2);
        movimentacaoService.registrarMovimentacao(outroUsuario, 999.0, "Movimentação de João", TipoMovimentacao.ENTRADA, 2);

        ResumoFinanceiro resumoMaria = movimentacaoService.gerarResumo(usuario.getId());

        assertEquals(3000.0, resumoMaria.getTotalEntradas(), 0.001);
    }

    /**
     * Cobre a funcionalidade adicionada na Etapa 9 (listarMovimentacoes),
     * necessária para o dashboard web exibir o extrato completo, algo que
     * as Etapas 6/7 nunca precisaram fazer.
     */
    @Test
    void deveListarMovimentacoesDoUsuarioOrdenadasEIsoladasPorUsuario() {
        Usuario outroUsuario = new Usuario(2, "João Souza", "joao@teste.com", "abcdef");

        movimentacaoService.registrarMovimentacao(usuario, 3000.0, "Salário", TipoMovimentacao.ENTRADA, 2);
        movimentacaoService.registrarMovimentacao(usuario, 450.50, "Supermercado", TipoMovimentacao.SAIDA, 1);
        movimentacaoService.registrarMovimentacao(outroUsuario, 999.0, "Movimentação de João", TipoMovimentacao.ENTRADA, 2);

        var lancamentos = movimentacaoService.listarMovimentacoes(usuario.getId());

        assertEquals(2, lancamentos.size());
        assertTrue(lancamentos.stream().allMatch(m -> m.getIdUsuario() == usuario.getId()));
    }

    /**
     * Regressão do bug encontrado na Etapa 9: MovimentacaoDAO.salvar não
     * recuperava o id gerado pelo banco. A implementação em memória já
     * fazia isso corretamente desde a Etapa 6, mas este teste documenta o
     * comportamento esperado explicitamente, já que ele é o contrato que
     * a implementação JDBC também precisa cumprir.
     */
    @Test
    void movimentacaoRegistradaDeveTerIdPreenchidoAposSalvar() {
        var movimentacao = movimentacaoService.registrarMovimentacao(
                usuario, 100.0, "Teste de id gerado", TipoMovimentacao.ENTRADA, 1);

        assertTrue(movimentacao.getId() > 0, "o id deveria ter sido preenchido após salvar");
    }
}
