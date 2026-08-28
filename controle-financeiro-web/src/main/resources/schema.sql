-- ============================================================
-- schema.sql — compatível com H2 (perfil padrão) e MySQL (perfil
-- "mysql"), sem sintaxe específica de nenhum dos dois bancos.
--
-- Os nomes de coluna e tabela seguem exatamente o que as classes
-- JDBC do módulo controle-financeiro-core (UsuarioDAO, CategoriaDAO,
-- MovimentacaoDAO — Etapas 6/7) já esperam nas suas consultas SQL.
-- ============================================================

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario  INT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(120) NOT NULL,
    email       VARCHAR(160) NOT NULL UNIQUE,
    senha       VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nome         VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS movimentacao (
    id_movimentacao INT AUTO_INCREMENT PRIMARY KEY,
    valor           DECIMAL(12,2) NOT NULL,
    descricao       VARCHAR(200) NOT NULL,
    data            DATE NOT NULL,
    tipo            INT NOT NULL,
    id_usuario      INT NOT NULL,
    id_categoria    INT NOT NULL,
    CONSTRAINT fk_movimentacao_usuario
        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_movimentacao_categoria
        FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);
