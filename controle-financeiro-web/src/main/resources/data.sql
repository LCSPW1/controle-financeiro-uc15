-- ============================================================
-- data.sql — categorias padrão, com os mesmos ids e nomes usados
-- em InMemoryCategoriaRepository (controle-financeiro-core), para
-- manter os dados de teste consistentes entre os módulos.
-- ============================================================

INSERT INTO categoria (id_categoria, nome) VALUES (1, 'Alimentação');
INSERT INTO categoria (id_categoria, nome) VALUES (2, 'Salário');
INSERT INTO categoria (id_categoria, nome) VALUES (3, 'Transporte');
INSERT INTO categoria (id_categoria, nome) VALUES (4, 'Lazer');
INSERT INTO categoria (id_categoria, nome) VALUES (5, 'Saúde');
INSERT INTO categoria (id_categoria, nome) VALUES (6, 'Moradia');
INSERT INTO categoria (id_categoria, nome) VALUES (7, 'Outros');
