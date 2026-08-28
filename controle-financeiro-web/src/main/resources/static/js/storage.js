/**
 * storage.js
 *
 * Na Etapa 8, este arquivo simulava um "banco de dados" com localStorage,
 * já que o enunciado daquela etapa dispensava back-end. Na Etapa 9, com o
 * back-end Spring REST no ar, ele foi reescrito para falar com a API real
 * via fetch() — mas a interface pública (os nomes das funções chamadas
 * pelas páginas) foi mantida praticamente igual de propósito, para que
 * login.js, cadastro.js, dashboard.js e nova-movimentacao.js precisassem
 * de poucas mudanças.
 *
 * Diferença importante: toda chamada de rede é assíncrona. Onde a versão
 * da Etapa 8 fazia `const usuario = CFStorage.autenticar(...)`, agora é
 * preciso `const usuario = await CFStorage.autenticar(...)` — por isso os
 * scripts de cada página também precisaram ser ajustados para usar
 * async/await (ver login.js, cadastro.js, etc.).
 *
 * A sessão (usuário logado) continua guardada no localStorage do
 * navegador — isso é só "quem sou eu neste navegador", não dado de
 * negócio, então não precisa vir do servidor. Em uma evolução futura, o
 * ideal seria substituir isso por um token de sessão (JWT) emitido pelo
 * back-end; ver observação no README sobre próximos passos de segurança.
 */

const CFStorage = (() => {
  const BASE_URL = "/api";
  const CHAVE_SESSAO = "cf_sessao";

  async function requisitar(caminho, opcoes = {}) {
    const resposta = await fetch(BASE_URL + caminho, {
      headers: { "Content-Type": "application/json" },
      ...opcoes,
    });

    let corpo = null;
    try {
      corpo = await resposta.json();
    } catch (e) {
      corpo = null;
    }

    if (!resposta.ok) {
      const mensagem = corpo && corpo.mensagem ? corpo.mensagem : "Erro inesperado ao falar com o servidor.";
      const erro = new Error(mensagem);
      erro.status = resposta.status;
      throw erro;
    }

    return corpo;
  }

  // ---------------- Usuário / autenticação ----------------

  async function cadastrarUsuario({ nome, email, senha }) {
    return requisitar("/usuarios", {
      method: "POST",
      body: JSON.stringify({ nome, email, senha }),
    });
  }

  async function autenticar(email, senha) {
    try {
      return await requisitar("/auth/login", {
        method: "POST",
        body: JSON.stringify({ email, senha }),
      });
    } catch (erro) {
      if (erro.status === 401) return null; // credenciais inválidas
      throw erro;
    }
  }

  // ---------------- Sessão (guardada no navegador) ----------------

  function getSessao() {
    const bruto = localStorage.getItem(CHAVE_SESSAO);
    return bruto ? JSON.parse(bruto) : null;
  }

  function login(usuario) {
    localStorage.setItem(CHAVE_SESSAO, JSON.stringify(usuario));
  }

  function logout() {
    localStorage.removeItem(CHAVE_SESSAO);
  }

  // ---------------- Categoria ----------------

  async function getCategorias() {
    return requisitar("/categorias");
  }

  // ---------------- Movimentação ----------------

  async function getMovimentacoesDoUsuario(idUsuario) {
    return requisitar(`/movimentacoes?idUsuario=${idUsuario}`);
  }

  async function registrarMovimentacao({ idUsuario, valor, descricao, tipo, idCategoria }) {
    return requisitar("/movimentacoes", {
      method: "POST",
      body: JSON.stringify({ idUsuario, valor, descricao, tipo, idCategoria }),
    });
  }

  async function gerarResumo(idUsuario) {
    return requisitar(`/resumo?idUsuario=${idUsuario}`);
  }

  return {
    cadastrarUsuario,
    autenticar,
    getSessao,
    login,
    logout,
    getCategorias,
    getMovimentacoesDoUsuario,
    registrarMovimentacao,
    gerarResumo,
  };
})();
