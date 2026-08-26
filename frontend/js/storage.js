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

  function cadastrarUsuario({ nome, email, senha }) {
    const usuarios = getUsuarios();
    const novo = { id: proximoId(usuarios), nome, email, senha };
    usuarios.push(novo);
    salvar(KEYS.usuarios, usuarios);
    return novo;
  }

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
      tipo,
      idCategoria,
      data: new Date().toISOString(),
    };
    movimentacoes.push(nova);
    salvar(KEYS.movimentacoes, movimentacoes);
    return nova;
  }

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
