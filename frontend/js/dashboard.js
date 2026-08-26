document.addEventListener("DOMContentLoaded", () => {
  const usuario = CFStorage.getSessao();
  if (!usuario) {
    window.location.href = "index.html";
    return;
  }

  const formatador = new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL",
  });

  document.getElementById("nome-usuario").textContent = usuario.nome.split(" ")[0];

  document.getElementById("btn-sair").addEventListener("click", () => {
    CFStorage.logout();
    window.location.href = "index.html";
  });

  function renderResumo() {
    const resumo = CFStorage.gerarResumo(usuario.id);

    document.getElementById("valor-entradas").textContent = formatador.format(resumo.totalEntradas);
    document.getElementById("valor-saidas").textContent = formatador.format(resumo.totalSaidas);
    document.getElementById("valor-saldo").textContent = formatador.format(resumo.saldo);

    const stubSaldo = document.getElementById("stub-saldo");
    stubSaldo.classList.toggle("positivo", !resumo.isSaldoNegativo);
    stubSaldo.classList.toggle("negativo", resumo.isSaldoNegativo);
  }

  function linhaTabela(mov) {
    const dataFormatada = new Date(mov.data).toLocaleDateString("pt-BR");
    const rotuloTipo = mov.tipo === "ENTRADA" ? "Entrada" : "Saída";
    const sinal = mov.tipo === "ENTRADA" ? "+ " : "− ";

    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${dataFormatada}</td>
      <td>${escapeHtml(mov.descricao)}</td>
      <td>${escapeHtml(CFStorage.nomeCategoria(mov.idCategoria))}</td>
      <td><span class="badge ${mov.tipo === "ENTRADA" ? "entrada" : "saida"}">${rotuloTipo}</span></td>
      <td class="valor ${mov.tipo === "ENTRADA" ? "entrada" : "saida"}">${sinal}${formatador.format(mov.valor)}</td>
    `;
    return tr;
  }

  function escapeHtml(texto) {
    const div = document.createElement("div");
    div.textContent = texto;
    return div.innerHTML;
  }

  function renderTabela(filtro) {
    const corpo = document.getElementById("corpo-tabela");
    const wrapTabela = document.getElementById("wrap-tabela");
    const estadoVazio = document.getElementById("estado-vazio");

    const todas = CFStorage.getMovimentacoesDoUsuario(usuario.id).sort(
      (a, b) => new Date(b.data) - new Date(a.data)
    );

    if (todas.length === 0) {
      wrapTabela.hidden = true;
      estadoVazio.hidden = false;
      return;
    }
    wrapTabela.hidden = false;
    estadoVazio.hidden = true;

    const filtradas = filtro === "todos" ? todas : todas.filter((m) => m.tipo === filtro);

    corpo.innerHTML = "";
    if (filtradas.length === 0) {
      const tr = document.createElement("tr");
      tr.innerHTML = `<td colspan="5" class="text-muted" style="text-align:center; padding: var(--space-6);">Nenhum lançamento neste filtro.</td>`;
      corpo.appendChild(tr);
      return;
    }
    filtradas.forEach((mov) => corpo.appendChild(linhaTabela(mov)));
  }

  document.querySelectorAll(".filter-tabs button").forEach((botao) => {
    botao.addEventListener("click", () => {
      document
        .querySelectorAll(".filter-tabs button")
        .forEach((b) => b.setAttribute("aria-pressed", "false"));
      botao.setAttribute("aria-pressed", "true");
      renderTabela(botao.dataset.filtro);
    });
  });

  function mostrarToastSeNecessario() {
    const mensagem = sessionStorage.getItem("cf_toast");
    if (!mensagem) return;
    sessionStorage.removeItem("cf_toast");

    const toast = document.getElementById("toast");
    toast.textContent = mensagem;
    toast.classList.add("is-visible");
    setTimeout(() => toast.classList.remove("is-visible"), 2600);
  }

  renderResumo();
  renderTabela("todos");
  mostrarToastSeNecessario();
});
