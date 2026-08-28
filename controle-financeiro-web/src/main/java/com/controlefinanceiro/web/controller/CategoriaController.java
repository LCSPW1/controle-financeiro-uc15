package com.controlefinanceiro.web.controller;

import com.controlefinanceiro.core.service.CategoriaService;
import com.controlefinanceiro.web.controller.dto.CategoriaResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        return categoriaService.listarCategorias().stream()
                .map(CategoriaResponse::de)
                .toList();
    }
}
