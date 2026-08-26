# controle-financeiro-core

Projeto Java (Maven) criado nas Etapas 6 e 7 do Projeto Integrador,
resultado da refatoração do sistema desktop `controle-financeiro-java`
(ETAPA_4).

O objetivo é separar as **regras de negócio** (validações, cálculo de
saldo, cadastro/login) da **interface gráfica Java Swing** original,
aplicando princípios SOLID, eliminando code smells e cobrindo as regras
com testes unitários (JUnit 5), para que essa lógica possa ser
reaproveitada tanto pela aplicação desktop quanto pelo futuro sistema web.

## Como abrir no NetBeans

1. `File → Open Project...`
2. Selecione a pasta `controle-financeiro-core` (a que contém o `pom.xml`).
3. O NetBeans reconhece automaticamente como projeto Maven — devem
   aparecer os nós "Source Packages" (`src/main/java`), "Test Packages"
   (`src/test/java`) e "Dependencies" na árvore do projeto.
4. Clique com o botão direito no projeto → `Clean and Build`.

## Como rodar

- **Aplicação (testes manuais do main()):** clique com o botão direito no
  arquivo `ControleFinanceiroCoreApp.java` → `Run File`. Isso executa os
  9 cenários de teste manual descritos no console (ver Etapa 6).
- **Testes unitários JUnit:** clique com o botão direito no projeto →
  `Test` (ou `Alt+F6`), ou rode pelo terminal:
  ```bash
  mvn test
  ```

Este projeto **não depende do driver JDBC do MySQL para compilar nem para
rodar os testes**, pois tanto o `main()` quanto os testes JUnit usam
repositórios em memória. O driver do MySQL só é necessário se você quiser
executar de fato as classes em `repository/jdbc` contra um banco real —
veja o comentário no `pom.xml` para habilitá-lo.

## Configuração do banco de dados (opcional, para uso real com MySQL)

1. Copie `src/main/resources/db.properties.example` para
   `src/main/resources/db.properties`.
2. Preencha `db.url`, `db.user` e `db.password` com as credenciais do seu
   ambiente.
3. Descomente a dependência do `mysql-connector-j` no `pom.xml`.
4. O arquivo `db.properties` está no `.gitignore` e **não deve ser
   commitado**.

## Estrutura de pacotes

```
src/main/java/com.controlefinanceiro.core
├── model            → Usuario, Categoria, Movimentacao, TipoMovimentacao, ResumoFinanceiro
├── exception         → ValidacaoException, AutenticacaoException, PersistenciaException
├── repository        → interfaces (contratos) dos repositórios
├── repository.jdbc   → implementação real com MySQL (mesmas regras do projeto desktop)
├── repository.memory → implementação em memória (usada nos testes)
├── infra             → configuração e conexão com o banco de dados
├── service           → regras de negócio extraídas das telas Swing
└── app               → ControleFinanceiroCoreApp (main() com testes manuais)

src/test/java/com.controlefinanceiro.core
├── model             → ResumoFinanceiroTest (cálculo de saldo)
└── service           → UsuarioServiceTest, CategoriaServiceTest, MovimentacaoServiceTest
```

## Documentação

- **Relatório da Etapa 6** (SOLID e refatorações): `RELATORIO_ETAPA6.docx`
  (entregue junto com a Etapa 6, fora deste repositório de código).
- **Plano de Testes da Etapa 7**: `docs/PLANO_DE_TESTES_ETAPA7.docx`
- **Instruções de Git/GitHub**: `INSTRUCOES_GIT.md`
