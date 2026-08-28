package com.controlefinanceiro.core.repository.memory;

import com.controlefinanceiro.core.model.Usuario;
import com.controlefinanceiro.core.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryUsuarioRepository implements UsuarioRepository {

    private final List<Usuario> usuarios = new ArrayList<>();
    private final AtomicInteger proximoId = new AtomicInteger(1);

    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void salvar(Usuario usuario) {
        usuario.setId(proximoId.getAndIncrement());
        usuarios.add(usuario);
    }

    @Override
    public boolean existeEmail(String email) {
        return usuarios.stream().anyMatch(u -> u.getEmail().equals(email));
    }
}
