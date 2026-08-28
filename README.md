# controle-financeiro-webapp

Projeto Java Web (Spring Boot / Spring REST) da **Etapa 9** do Projeto
Integrador — a etapa final de integração, que reúne:

- **`controle-financeiro-core`** (Etapas 6 e 7): regras de negócio, models,
  DAOs JDBC e testes unitários JUnit 5.
- **`controle-financeiro-web`** (Etapa 9, novo): back-end Spring REST que
  conecta o core a um banco de dados relacional real e expõe uma API
  consumida pelo front-end da Etapa 8, agora servido pelo próprio Spring
  Boot.

É um projeto **Maven multi-módulo**: a pasta raiz tem um `pom.xml`
agregador, e cada subpasta (`controle-financeiro-core`,
`controle-financeiro-web`) é um módulo com seu próprio `pom.xml`.

## Como abrir no NetBeans

1. `File → Open Project...` → selecione **a pasta raiz**
   (`controle-financeiro-webapp`, a que contém o `pom.xml` agregador).
2. O NetBeans reconhece automaticamente os dois módulos como subprojetos.
3. Botão direito no projeto raiz → **Clean and Build**. O Maven compila
   primeiro o `controle-financeiro-core` e depois o `controle-financeiro-web`
   (que depende dele), na ordem correta, sozinho.

> **A primeira build precisa de internet**, para o Maven baixar as
> dependências do Spring Boot (~40 bibliotecas) do repositório central.
> Depois da primeira vez, elas ficam em cache local (`~/.m2`) e as builds
> seguintes funcionam offline.

## Como rodar a aplicação

Pela linha de comando, na pasta raiz:

```bash
cd controle-financeiro-web
mvn spring-boot:run
```

Ou, pelo NetBeans: abra o módulo `controle-financeiro-web`, clique com o
botão direito em `ControleFinanceiroWebApplication.java` → **Run File**.

Depois de subir (procure a linha `Started ControleFinanceiroWebApplication`
no console), acesse:

- **http://localhost:8080/index.html** — a aplicação (login)
- **http://localhost:8080/h2-console** — console do banco H2 (só no perfil
  padrão; JDBC URL: `jdbc:h2:mem:controlefinanceiro`, usuário `sa`, senha em
  branco)

Por padrão, a aplicação usa um **banco H2 embutido em memória** — não
precisa instalar nada. Os dados (usuários, categorias, movimentações) são
recriados a cada reinício, a partir de
`controle-financeiro-web/src/main/resources/schema.sql` e `data.sql`.

### Usando MySQL em vez do H2 (opcional)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

Antes, crie o banco e rode `schema.sql`/`data.sql` manualmente contra o seu
MySQL (veja `application-mysql.properties` para os detalhes e o aviso sobre
`spring.sql.init.mode=never`).

## Endpoints da API REST

| Método | Caminho | Descrição |
|---|---|---|
| `POST` | `/api/usuarios` | Cadastra um novo usuário |
| `POST` | `/api/auth/login` | Autentica (e-mail + senha) |
| `GET` | `/api/categorias` | Lista as categorias disponíveis |
| `POST` | `/api/movimentacoes` | Registra uma entrada ou saída |
| `GET` | `/api/movimentacoes?idUsuario=1` | Lista os lançamentos de um usuário |
| `GET` | `/api/resumo?idUsuario=1` | Totais de entrada/saída e saldo |

Erros seguem o formato `{"mensagem": "..."}`, com o código HTTP mapeado a
partir das exceções do core (`GlobalExceptionHandler`):
`ValidacaoException` → 400, `AutenticacaoException` → 401,
`PersistenciaException` → 500.

## O que mudou em relação às etapas anteriores

Esta etapa exigiu alguns ajustes reais no `controle-financeiro-core`, que
antes só era exercitado por testes locais (JUnit, `main()`) e nunca tinha
sido integrado com uma aplicação de verdade. Documentado em detalhe em
`docs/EVIDENCIAS_TESTES_E_BUGTRACKING.docx`, em resumo:

1. **Senhas agora usam hash (PBKDF2)** em vez de comparação em texto puro —
   `UsuarioRepository.autenticar(email, senha)` foi substituído por
   `buscarPorEmail(email)`, e a verificação da senha passou para
   `UsuarioService`, usando a nova interface `PasswordHasher`.
2. **`MovimentacaoService` ganhou `listarMovimentacoes()`** — as Etapas 6/7
   só precisavam de `gerarResumo()` (totais); o dashboard web precisa da
   lista completa de lançamentos.
3. **Bug corrigido:** `UsuarioDAO.salvar` e `MovimentacaoDAO.salvar` nunca
   recuperavam o id gerado pelo banco (`Statement.RETURN_GENERATED_KEYS`).
   Passou despercebido nas Etapas 6/7 porque os testes usavam a
   implementação em memória, que já gerava o id sozinha.
4. **Front-end (`js/storage.js` e os scripts de cada página) reescrito**
   para usar `fetch()` (assíncrono) em vez de `localStorage`.

## Estrutura

```
controle-financeiro-webapp/
├── pom.xml                          (agregador Maven)
├── controle-financeiro-core/        (Etapas 6/7 — regras de negócio)
│   └── ... (ver README anterior do core, se necessário)
├── controle-financeiro-web/         (Etapa 9 — Spring Boot)
│   └── src/main/
│       ├── java/com/controlefinanceiro/web/
│       │   ├── ControleFinanceiroWebApplication.java
│       │   ├── config/RepositoryBeansConfig.java   ← integração com o core
│       │   ├── controller/                          ← endpoints REST
│       │   └── exception/GlobalExceptionHandler.java
│       └── resources/
│           ├── application.properties (H2, padrão)
│           ├── application-mysql.properties (opcional)
│           ├── schema.sql / data.sql
│           └── static/                               ← front-end da Etapa 8
└── docs/
    ├── wireframes/                   (Etapa 8)
    ├── EVIDENCIAS_TESTES_E_BUGTRACKING.docx
    └── EVIDENCIAS_VERSIONAMENTO.docx
```

## Limitações conhecidas / próximos passos

- **Sessão simplificada**: após o login, o front-end guarda os dados do
  usuário no `localStorage` do navegador, sem token de sessão (JWT) ou
  cookie de autenticação. Qualquer requisição para `/api/movimentacoes` ou
  `/api/resumo` aceita qualquer `idUsuario` informado, sem verificar se
  pertence a quem está de fato logado. Para um ambiente de produção real,
  o próximo passo seria adicionar Spring Security com autenticação por
  token, validando o usuário em cada requisição no servidor.
- **Pool de conexões**: `ConexaoDB` (core) abre uma conexão nova por
  chamada via `DriverManager`, sem pool. Funciona bem na escala de um
  projeto acadêmico; um pool (HikariCP, já presente no classpath via
  `spring-boot-starter-jdbc`) traria melhor desempenho sob carga.
