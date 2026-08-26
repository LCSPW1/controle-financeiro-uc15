package com.controlefinanceiro.core.service;

import com.controlefinanceiro.core.exception.ValidacaoException;
import com.controlefinanceiro.core.model.Categoria;
import com.controlefinanceiro.core.repository.memory.InMemoryCategoriaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários das regras de negócio de CategoriaService.
 */
class CategoriaServiceTest {

    private CategoriaService categoriaService;

    @BeforeEach
    void configurar() {
        InMemoryCategoriaRepository categoriaRepository = new InMemoryCategoriaRepository();
        categoriaRepository.adicionar(new Categoria(1, "Alimentação"));
        categoriaRepository.adicionar(new Categoria(2, "Salário"));
        categoriaService = new CategoriaService(categoriaRepository);
    }

    @Test
    void deveListarTodasAsCategoriasCadastradas() {
        assertEquals(2, categoriaService.listarCategorias().size());
    }

    @Test
    void deveRetornarCategoriaValidaQuandoIdExiste() {
        Categoria categoria = categoriaService.buscarCategoriaValida(1);

        assertEquals("Alimentação", categoria.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoIdForZeroPlaceholder() {
        assertThrows(ValidacaoException.class, () -> categoriaService.buscarCategoriaValida(0));
    }

    @Test
    void deveLancarExcecaoQuandoIdNaoExistir() {
        assertThrows(ValidacaoException.class, () -> categoriaService.buscarCategoriaValida(999));
    }
}
