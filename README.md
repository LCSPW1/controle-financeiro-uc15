# controle-financeiro-core

Projeto Java criado na **Etapa 6** do Projeto Integrador, resultado da
refatoração do sistema desktop `controle-financeiro-java` (ETAPA_4).

O objetivo é separar as **regras de negócio** (validações, cálculo de saldo,
cadastro/login) da **interface gráfica Java Swing** original, aplicando
princípios SOLID e eliminando code smells, para que essa lógica possa ser
reaproveitada tanto pela aplicação desktop quanto pelo futuro sistema web.

## Como abrir no NetBeans

1. `File → Open Project...`
2. Selecione a pasta `controle-financeiro-core`.
3. Clique com o botão direito no projeto → `Run` (ou `Clean and Build`).
4. A classe principal (`main.class`) já está configurada como
   `com.controlefinanceiro.core.app.ControleFinanceiroCoreApp`, que executa
   os testes automatizados do projeto no console.

Este projeto **não depende do driver JDBC do MySQL para compilar nem para
rodar os testes**, pois os testes usam repositórios em memória. O driver só
é necessário se você quiser executar de fato as classes em
`repository/jdbc` contra um banco MySQL real (veja `nbproject/project.properties`).

## Configuração do banco de dados (opcional, para uso real com MySQL)

1. Copie `src/db.properties.example` para `src/db.properties`.
2. Preencha `db.url`, `db.user` e `db.password` com as credenciais do seu ambiente.
3. O arquivo `src/db.properties` está no `.gitignore` e **não deve ser commitado**.

## Estrutura de pacotes

```
com.controlefinanceiro.core
├── model            → Usuario, Categoria, Movimentacao, TipoMovimentacao, ResumoFinanceiro
├── exception         → ValidacaoException, AutenticacaoException, PersistenciaException
├── repository        → interfaces (contratos) dos repositórios
├── repository.jdbc   → implementação real com MySQL (mesmas regras do projeto desktop)
├── repository.memory → implementação em memória (usada nos testes do main())
├── infra             → configuração e conexão com o banco de dados
├── service           → regras de negócio extraídas das telas Swing
└── app               → ControleFinanceiroCoreApp (main() com os testes)
```

Veja o relatório da Etapa 6 (`RELATORIO_ETAPA6.docx`) para o detalhamento
dos princípios SOLID aplicados e das refatorações realizadas.
