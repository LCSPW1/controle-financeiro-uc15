package com.controlefinanceiro.core.repository.memory;

import com.controlefinanceiro.core.model.Categoria;
import com.controlefinanceiro.core.repository.CategoriaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryCategoriaRepository implements CategoriaRepository {

    private final List<Categoria> categorias = new ArrayList<>();

    public void adicionar(Categoria categoria) {
        categorias.add(categoria);
    }

    @Override
    public List<Categoria> listarTodas() {
        return new ArrayList<>(categorias);
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) {
        return categorias.stream().filter(c -> c.getId() == id).findFirst();
    }
}
