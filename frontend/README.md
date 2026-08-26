# Frontend — Sistema Web de Controle Financeiro

Front-end estático (HTML + CSS + JavaScript, sem back-end) desenvolvido na
Etapa 8 do Projeto Integrador. Reproduz na web as mesmas telas e regras de
negócio já implementadas e testadas na camada `controle-financeiro-core`
(Etapas 6 e 7), preparando o terreno para a futura integração com uma API.

## Como abrir

Não é necessário nenhum servidor ou build. Basta abrir `index.html`
diretamente no navegador (duplo clique, ou arrastar para a janela do
navegador). Todos os caminhos são relativos.

> Funciona melhor com conexão à internet na primeira vez, pois as fontes
> (Fraunces, Inter, IBM Plex Mono) são carregadas do Google Fonts. Sem
> internet, o navegador usa as fontes padrão do sistema como alternativa —
> o layout continua funcional.

## Fluxo de telas

1. **`index.html`** — Login. Autentica contra os usuários cadastrados
   (armazenados no `localStorage` do navegador).
2. **`cadastro.html`** — Criação de conta (nome, e-mail, senha e
   confirmação).
3. **`dashboard.html`** — Painel principal: resumo do mês (entradas,
   saídas, saldo) e lista de lançamentos, com filtro por tipo.
4. **`nova-movimentacao.html`** — Formulário de registro de entrada ou
   saída, com seleção de categoria.

Todas as páginas (exceto login/cadastro) exigem uma sessão ativa — se você
abrir `dashboard.html` diretamente sem estar logado, é redirecionado para
o login automaticamente.

## Por que não há back-end

Conforme o próprio enunciado da Etapa 8, a conexão com banco de dados fica
para uma etapa futura. Para que as telas não ficassem estáticas demais, os
dados (usuários, categorias, movimentações e sessão) são simulados com
`localStorage` do navegador, em `js/storage.js`. Essa é a única peça que
precisará ser trocada por chamadas a uma API real (`fetch`) quando o
back-end existir — nenhuma página HTML ou regra de validação deve mudar.

## Regras de negócio replicadas do `controle-financeiro-core`

As validações em JavaScript (`js/validation.js` + scripts de cada página)
reproduzem, no cliente, as mesmas regras já implementadas e testadas nas
Etapas 6 e 7:

| Regra | Onde está no core (Java) | Onde está aqui (JS) |
|---|---|---|
| Campos obrigatórios, e-mail válido | `UsuarioService.cadastrar/autenticar` | `cadastro.js`, `login.js` |
| E-mail duplicado | `UsuarioService.cadastrar` | `cadastro.js` (`CFStorage.existeEmail`) |
| Categoria obrigatória (rejeita id 0) | `CategoriaService.buscarCategoriaValida` | `nova-movimentacao.js` |
| Valor > 0, descrição obrigatória | `MovimentacaoService.registrarMovimentacao` | `nova-movimentacao.js` |
| Saldo = entradas − saídas | `ResumoFinanceiro.getSaldo()` | `CFStorage.gerarResumo()` |
| Resumo isolado por usuário (bug corrigido na Etapa 6) | `MovimentacaoRepository.listarPorUsuarioETipo` | `CFStorage.getMovimentacoesDoUsuario` |

**Importante:** validação em JavaScript é só uma camada de conveniência
para o usuário. Quando o back-end real existir, ele precisa repetir essas
mesmas checagens — validação só no cliente pode ser burlada.

## Estrutura de arquivos

```
frontend/
├── index.html                  → login
├── cadastro.html                → criação de conta
├── dashboard.html                → painel principal
├── nova-movimentacao.html        → registro de entrada/saída
├── css/
│   ├── reset.css                → normalização entre navegadores
│   ├── style.css                 → tokens de design, tipografia, layout
│   ├── components.css            → botões, cards, tabela, badges, alertas
│   └── forms.css                 → campos, validação visual, toggle
├── js/
│   ├── storage.js                → dados mock (localStorage)
│   ├── validation.js             → validadores reutilizáveis
│   ├── login.js
│   ├── cadastro.js
│   ├── dashboard.js
│   └── nova-movimentacao.js
└── wireframes/
    ├── wireframes.html           → galeria com os 4 wireframes
    ├── wireframe-login.svg
    ├── wireframe-cadastro-usuario.svg
    ├── wireframe-dashboard.svg
    └── wireframe-cadastro-movimentacao.svg
```

## Identidade visual

Direção de design: "caderneta / livro-razão" — referência ao extrato
bancário em papel. Papel-cru de fundo, tinta verde-escura para texto,
verde-cédula para entradas, tijolo para saídas e dourado para destaque.
Números em fonte monoespaçada tabular (como um extrato real). O
elemento-assinatura são os cartões de resumo do dashboard em estilo
"canhoto de talão" (borda tracejada, régua colorida indicando o sinal do
valor).

Tipografia: **Fraunces** (serifada, títulos) + **Inter** (interface) +
**IBM Plex Mono** (valores em R$).
