package com.controlefinanceiro.core.service;

import com.controlefinanceiro.core.exception.ValidacaoException;
import com.controlefinanceiro.core.model.Categoria;
import com.controlefinanceiro.core.repository.CategoriaRepository;

import java.util.List;

public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.listarTodas();
    }

    public Categoria buscarCategoriaValida(int idCategoria) {
        if (idCategoria <= 0) {
            throw new ValidacaoException("Selecione uma categoria válida!");
        }
        return categoriaRepository.buscarPorId(idCategoria)
                .orElseThrow(() -> new ValidacaoException("Categoria não encontrada."));
    }
}
