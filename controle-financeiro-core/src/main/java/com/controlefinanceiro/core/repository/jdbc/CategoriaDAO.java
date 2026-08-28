package com.controlefinanceiro.core.repository.jdbc;

import com.controlefinanceiro.core.exception.PersistenciaException;
import com.controlefinanceiro.core.infra.ConexaoDB;
import com.controlefinanceiro.core.model.Categoria;
import com.controlefinanceiro.core.repository.CategoriaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaDAO implements CategoriaRepository {

    private final ConexaoDB conexaoDB;

    public CategoriaDAO(ConexaoDB conexaoDB) {
        this.conexaoDB = conexaoDB;
    }

    @Override
    public List<Categoria> listarTodas() {
        String sql = "SELECT * FROM categoria ORDER BY nome ASC";
        List<Categoria> lista = new ArrayList<>();

        try (Connection conn = conexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar categorias.", e);
        }
        return lista;
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) {
        String sql = "SELECT * FROM categoria WHERE id_categoria = ?";

        try (Connection conn = conexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Categoria(rs.getInt("id_categoria"), rs.getString("nome")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar categoria por id.", e);
        }
    }
}
