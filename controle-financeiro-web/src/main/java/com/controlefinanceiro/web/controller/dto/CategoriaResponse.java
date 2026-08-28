package com.controlefinanceiro.web.controller.dto;

import com.controlefinanceiro.core.model.Categoria;

public record CategoriaResponse(int id, String nome) {

    public static CategoriaResponse de(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNome());
    }
}
