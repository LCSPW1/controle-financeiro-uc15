package com.controlefinanceiro.core.repository.jdbc;

import com.controlefinanceiro.core.exception.PersistenciaException;
import com.controlefinanceiro.core.infra.ConexaoDB;
import com.controlefinanceiro.core.model.Movimentacao;
import com.controlefinanceiro.core.model.TipoMovimentacao;
import com.controlefinanceiro.core.repository.MovimentacaoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO implements MovimentacaoRepository {

    private final ConexaoDB conexaoDB;

    public MovimentacaoDAO(ConexaoDB conexaoDB) {
        this.conexaoDB = conexaoDB;
    }

    @Override
    public void salvar(Movimentacao movimentacao) {
        String sql = """
                INSERT INTO movimentacao
                (valor, descricao, data, tipo, id_usuario, id_categoria)
                VALUES (?, ?, ?, ?, ?, ?)
            """;

        // Bug encontrado na Etapa 9 ao integrar com um banco real: esta
        // consulta nunca solicitava nem lia o id gerado pelo banco de
        // dados (Statement.RETURN_GENERATED_KEYS), então o objeto
        // Movimentacao retornado a quem chamava salvar() ficava sempre
        // com id = 0. Isso passou despercebido nas Etapas 6/7 porque os
        // testes usavam a implementação em memória, que já gera o id por
        // conta própria. Corrigido para recuperar e preencher o id real.
        try (Connection conn = conexaoDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDouble(1, movimentacao.getValor());
            ps.setString(2, movimentacao.getDescricao());
            ps.setDate(3, java.sql.Date.valueOf(movimentacao.getData()));
            ps.setInt(4, movimentacao.getTipo().getCodigo());
            ps.setInt(5, movimentacao.getIdUsuario());
            ps.setInt(6, movimentacao.getIdCategoria());

            ps.executeUpdate();

            try (ResultSet chaves = ps.getGeneratedKeys()) {
                if (chaves.next()) {
                    movimentacao.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar movimentação.", e);
        }
    }

    @Override
    public List<Movimentacao> listarPorUsuarioETipo(int idUsuario, TipoMovimentacao tipo) {
        String sql = "SELECT m.*, c.nome AS nome_categoria "
                + "FROM movimentacao m "
                + "INNER JOIN categoria c ON m.id_categoria = c.id_categoria "
                + "WHERE m.tipo = ? AND m.id_usuario = ?";

        List<Movimentacao> lista = new ArrayList<>();
        try (Connection conn = conexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tipo.getCodigo());
            stmt.setInt(2, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Movimentacao m = new Movimentacao();
                    m.setId(rs.getInt("id_movimentacao"));
                    m.setData(rs.getDate("data").toLocalDate());
                    m.setDescricao(rs.getString("descricao"));
                    m.setValor(rs.getDouble("valor"));
                    m.setTipo(tipo);
                    m.setIdUsuario(idUsuario);
                    m.setIdCategoria(rs.getInt("id_categoria"));
                    m.setNomeCategoria(rs.getString("nome_categoria"));
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar movimentações.", e);
        }
        return lista;
    }

    @Override
    public List<Movimentacao> listarPorUsuario(int idUsuario) {
        String sql = "SELECT m.*, c.nome AS nome_categoria "
                + "FROM movimentacao m "
                + "INNER JOIN categoria c ON m.id_categoria = c.id_categoria "
                + "WHERE m.id_usuario = ? "
                + "ORDER BY m.data DESC, m.id_movimentacao DESC";

        List<Movimentacao> lista = new ArrayList<>();
        try (Connection conn = conexaoDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Movimentacao m = new Movimentacao();
                    m.setId(rs.getInt("id_movimentacao"));
                    m.setData(rs.getDate("data").toLocalDate());
                    m.setDescricao(rs.getString("descricao"));
                    m.setValor(rs.getDouble("valor"));
                    m.setTipo(TipoMovimentacao.porCodigo(rs.getInt("tipo")));
                    m.setIdUsuario(idUsuario);
                    m.setIdCategoria(rs.getInt("id_categoria"));
                    m.setNomeCategoria(rs.getString("nome_categoria"));
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar movimentações do usuário.", e);
        }
        return lista;
    }
}
