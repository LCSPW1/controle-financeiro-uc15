package com.controlefinanceiro.core.repository.jdbc;

import com.controlefinanceiro.core.exception.PersistenciaException;
import com.controlefinanceiro.core.infra.ConexaoDB;
import com.controlefinanceiro.core.model.Usuario;
import com.controlefinanceiro.core.repository.UsuarioRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO implements UsuarioRepository {

    private final ConexaoDB conexaoDB;

    public UsuarioDAO(ConexaoDB conexaoDB) {
        this.conexaoDB = conexaoDB;
    }

    @Override
    public Usuario autenticar(String email, String senha) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";

        try (Connection conn = conexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, senha);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.setId(rs.getInt("id_usuario"));
                    u.setNome(rs.getString("nome"));
                    u.setEmail(rs.getString("email"));
                    u.setSenha(rs.getString("senha"));
                    return u;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao autenticar usuário.", e);
        }
    }

    @Override
    public void salvar(Usuario usuario) {
        String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";

        try (Connection conn = conexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenha());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar usuário.", e);
        }
    }

    @Override
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE email = ?";

        try (Connection conn = conexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao verificar e-mail existente.", e);
        }
    }
}
