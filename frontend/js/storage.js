/**
 * storage.js
 *
 * "Banco de dados" simulado no navegador (localStorage), usado apenas
 * para dar vida às páginas nesta etapa — o enunciado da Etapa 8 dispensa
 * explicitamente a conexão com um banco de dados real. As estruturas de
 * dados abaixo espelham de propósito os models já validados no projeto
 * controle-financeiro-core (Etapas 6 e 7): Usuario, Categoria,
 * Movimentacao (com TipoMovimentacao "ENTRADA"/"SAIDA") e o cálculo de
 * ResumoFinanceiro (saldo = entradas − saídas). Quando o back-end real for
 * implementado, este arquivo é o único que precisa ser substituído por
 * chamadas fetch() a uma API — nenhuma página HTML precisa mudar.
 */

const CFStorage = (() => {
  const KEYS = {
    usuarios: "cf_usuarios",
    categorias: "cf_categorias",
    movimentacoes: "cf_movimentacoes",
    sessao: "cf_sessao",
  };

  function ler(chave, valorPadrao) {
    try {
      const bruto = localStorage.getItem(chave);
      return bruto ? JSON.parse(bruto) : valorPadrao;
    } catch (e) {
      return valorPadrao;
    }
  }

  function salvar(chave, valor) {
    localStorage.setItem(chave, JSON.stringify(valor));
  }

  function proximoId(lista) {
    return lista.reduce((max, item) => Math.max(max, item.id), 0) + 1;
  }

  /** Garante que existam categorias padrão na primeira execução. */
  function init() {
    const categorias = ler(KEYS.categorias, null);
    if (!categorias) {
      salvar(KEYS.categorias, [
        { id: 1, nome: "Alimentação" },
        { id: 2, nome: "Salário" },
        { id: 3, nome: "Transporte" },
        { id: 4, nome: "Lazer" },
        { id: 5, nome: "Saúde" },
        { id: 6, nome: "Moradia" },
        { id: 7, nome: "Outros" },
      ]);
    }
    if (!ler(KEYS.usuarios, null)) {
      salvar(KEYS.usuarios, []);
    }
    if (!ler(KEYS.movimentacoes, null)) {
      salvar(KEYS.movimentacoes, []);
    }
  }

  // ---------------- Usuário ----------------

  function getUsuarios() {
    return ler(KEYS.usuarios, []);
  }

  function existeEmail(email) {
    return getUsuarios().some(
      (u) => u.email.toLowerCase() === email.toLowerCase()
    );
  }

  /** Espelha UsuarioService.cadastrar (Etapa 6/7). */
  function cadastrarUsuario({ nome, email, senha }) {
    const usuarios = getUsuarios();
    const novo = { id: proximoId(usuarios), nome, email, senha };
    usuarios.push(novo);
    salvar(KEYS.usuarios, usuarios);
    return novo;
  }

  /** Espelha UsuarioService.autenticar (Etapa 6/7). */
  function autenticar(email, senha) {
    return (
      getUsuarios().find(
        (u) => u.email.toLowerCase() === email.toLowerCase() && u.senha === senha
      ) || null
    );
  }

  // ---------------- Sessão ----------------

  function getSessao() {
    const sessao = ler(KEYS.sessao, null);
    if (!sessao) return null;
    return getUsuarios().find((u) => u.id === sessao.idUsuario) || null;
  }

  function login(usuario) {
    salvar(KEYS.sessao, { idUsuario: usuario.id });
  }

  function logout() {
    localStorage.removeItem(KEYS.sessao);
  }

  // ---------------- Categoria ----------------

  function getCategorias() {
    return ler(KEYS.categorias, []);
  }

  // ---------------- Movimentação ----------------

  function getMovimentacoesDoUsuario(idUsuario) {
    return ler(KEYS.movimentacoes, []).filter((m) => m.idUsuario === idUsuario);
  }

  /** Espelha MovimentacaoService.registrarMovimentacao (Etapa 6/7). */
  function registrarMovimentacao({ idUsuario, valor, descricao, tipo, idCategoria }) {
    const movimentacoes = ler(KEYS.movimentacoes, []);
    const nova = {
      id: proximoId(movimentacoes),
      idUsuario,
      valor,
      descricao,
      tipo, // "ENTRADA" | "SAIDA"
      idCategoria,
      data: new Date().toISOString(),
    };
    movimentacoes.push(nova);
    salvar(KEYS.movimentacoes, movimentacoes);
    return nova;
  }

  /**
   * Espelha MovimentacaoService.gerarResumo / model.ResumoFinanceiro
   * (Etapa 6/7): saldo = totalEntradas - totalSaidas. Assim como na
   * correção de bug aplicada na Etapa 6, o resumo é sempre calculado
   * filtrando apenas as movimentações do usuário logado.
   */
  function gerarResumo(idUsuario) {
    const minhas = getMovimentacoesDoUsuario(idUsuario);
    const totalEntradas = minhas
      .filter((m) => m.tipo === "ENTRADA")
      .reduce((soma, m) => soma + m.valor, 0);
    const totalSaidas = minhas
      .filter((m) => m.tipo === "SAIDA")
      .reduce((soma, m) => soma + m.valor, 0);

    return {
      totalEntradas,
      totalSaidas,
      saldo: totalEntradas - totalSaidas,
      isSaldoNegativo: totalEntradas - totalSaidas < 0,
    };
  }

  function nomeCategoria(idCategoria) {
    const categoria = getCategorias().find((c) => c.id === idCategoria);
    return categoria ? categoria.nome : "—";
  }

  return {
    init,
    getUsuarios,
    existeEmail,
    cadastrarUsuario,
    autenticar,
    getSessao,
    login,
    logout,
    getCategorias,
    getMovimentacoesDoUsuario,
    registrarMovimentacao,
    gerarResumo,
    nomeCategoria,
  };
})();

CFStorage.init();
