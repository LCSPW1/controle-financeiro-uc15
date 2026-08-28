/**
 * dashboard.js — painel principal.
 *
 * Reescrito na Etapa 9 para buscar dados reais da API (GET /api/resumo e
 * GET /api/movimentacoes) em vez de calcular tudo no navegador a partir do
 * localStorage. Duas diferenças importantes em relação à Etapa 8:
 *
 * 1) A API já devolve o nome da categoria dentro de cada movimentação
 *    (campo "categoria"), então não é mais preciso um "nomeCategoria()"
 *    local — essa junção agora acontece no SQL, dentro de
 *    MovimentacaoDAO.listarPorUsuario (core).
 *
 * 2) Bug encontrado nesta etapa: a data vem da API no formato
 *    "AAAA-MM-DD" (LocalDate do Java). Construir um objeto Date do
 *    JavaScript diretamente com "new Date('2026-08-27')" faz o navegador
 *    interpretar a data como meia-noite em UTC, o que pode exibir o dia
 *    ANTERIOR em fusos horários atrás de UTC (como o Brasil). A função
 *    parseDataLocal() abaixo evita isso, construindo a data a partir dos
 *    componentes ano/mês/dia diretamente no fuso horário local.
 */
document.addEventListener("DOMContentLoaded", async () => {
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

  function parseDataLocal(dataIso) {
    const [ano, mes, dia] = dataIso.split("-").map(Number);
    return new Date(ano, mes - 1, dia);
  }

  function renderResumo(resumo) {
    document.getElementById("valor-entradas").textContent = formatador.format(resumo.totalEntradas);
    document.getElementById("valor-saidas").textContent = formatador.format(resumo.totalSaidas);
    document.getElementById("valor-saldo").textContent = formatador.format(resumo.saldo);

    const stubSaldo = document.getElementById("stub-saldo");
    stubSaldo.classList.toggle("positivo", !resumo.saldoNegativo);
    stubSaldo.classList.toggle("negativo", resumo.saldoNegativo);
  }

  function linhaTabela(mov) {
    const dataFormatada = parseDataLocal(mov.data).toLocaleDateString("pt-BR");
    const rotuloTipo = mov.tipo === "ENTRADA" ? "Entrada" : "Saída";
    const sinal = mov.tipo === "ENTRADA" ? "+ " : "− ";

    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${dataFormatada}</td>
      <td>${escapeHtml(mov.descricao)}</td>
      <td>${escapeHtml(mov.categoria)}</td>
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

  function renderTabela(todas, filtro) {
    const corpo = document.getElementById("corpo-tabela");
    const wrapTabela = document.getElementById("wrap-tabela");
    const estadoVazio = document.getElementById("estado-vazio");

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

  function mostrarToastSeNecessario() {
    const mensagem = sessionStorage.getItem("cf_toast");
    if (!mensagem) return;
    sessionStorage.removeItem("cf_toast");

    const toast = document.getElementById("toast");
    toast.textContent = mensagem;
    toast.classList.add("is-visible");
    setTimeout(() => toast.classList.remove("is-visible"), 2600);
  }

  try {
    const [resumo, movimentacoes] = await Promise.all([
      CFStorage.gerarResumo(usuario.id),
      CFStorage.getMovimentacoesDoUsuario(usuario.id),
    ]);

    renderResumo(resumo);
    renderTabela(movimentacoes, "todos");

    document.querySelectorAll(".filter-tabs button").forEach((botao) => {
      botao.addEventListener("click", () => {
        document
          .querySelectorAll(".filter-tabs button")
          .forEach((b) => b.setAttribute("aria-pressed", "false"));
        botao.setAttribute("aria-pressed", "true");
        renderTabela(movimentacoes, botao.dataset.filtro);
      });
    });

    mostrarToastSeNecessario();
  } catch (erro) {
    console.error("Falha ao carregar o painel:", erro);
    document.querySelector(".app-main").innerHTML =
      '<p class="text-muted">Não foi possível carregar seus dados agora. Tente recarregar a página.</p>';
  }
});
