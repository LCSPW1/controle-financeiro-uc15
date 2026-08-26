package com.controlefinanceiro.core.repository;

import com.controlefinanceiro.core.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {

    List<Categoria> listarTodas();

    Optional<Categoria> buscarPorId(int id);
}
