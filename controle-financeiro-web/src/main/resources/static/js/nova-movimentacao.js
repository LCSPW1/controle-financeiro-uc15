/**
 * nova-movimentacao.js — validação e registro de entrada/saída.
 *
 * Reescrito na Etapa 9: o carregamento das categorias e o envio do
 * formulário agora são chamadas assíncronas à API (GET /api/categorias e
 * POST /api/movimentacoes). As mesmas regras de validação client-side da
 * Etapa 8 continuam aqui como camada de conveniência, mas quem tem a
 * palavra final é sempre o MovimentacaoService no servidor.
 */
document.addEventListener("DOMContentLoaded", async () => {
  const usuario = CFStorage.getSessao();
  if (!usuario) {
    window.location.href = "index.html";
    return;
  }

  document.getElementById("nome-usuario").textContent = usuario.nome.split(" ")[0];

  document.getElementById("btn-sair").addEventListener("click", () => {
    CFStorage.logout();
    window.location.href = "index.html";
  });

  const selectCategoria = document.getElementById("categoria");
  try {
    const categorias = await CFStorage.getCategorias();
    categorias.forEach((categoria) => {
      const option = document.createElement("option");
      option.value = String(categoria.id);
      option.textContent = categoria.nome;
      selectCategoria.appendChild(option);
    });
  } catch (erro) {
    console.error("Falha ao carregar categorias:", erro);
  }

  function paraNumero(valorTexto) {
    const normalizado = valorTexto
      .trim()
      .replace(/\./g, "")
      .replace(",", ".");
    return Number(normalizado);
  }

  const form = document.getElementById("form-movimentacao");
  const alertErro = document.getElementById("alert-erro");

  const campoValor = document.getElementById("field-valor");
  const campoDescricao = document.getElementById("field-descricao");
  const campoCategoria = document.getElementById("field-categoria");

  const inputValor = document.getElementById("valor");
  const inputDescricao = document.getElementById("descricao");
  const botaoSubmit = form.querySelector("button[type=submit]");

  form.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    CFValidation.clearAllErrors(form);
    CFValidation.hideAlert(alertErro);

    let valido = true;
    const valorNumerico = paraNumero(inputValor.value);

    if (!CFValidation.isRequired(inputValor.value) || !CFValidation.isPositiveNumber(valorNumerico)) {
      CFValidation.setFieldError(campoValor, "Informe um valor numérico maior que zero.");
      valido = false;
    }

    if (!CFValidation.isRequired(inputDescricao.value)) {
      CFValidation.setFieldError(campoDescricao, "Informe a descrição.");
      valido = false;
    }

    const idCategoria = Number(selectCategoria.value);
    if (!idCategoria || idCategoria <= 0) {
      CFValidation.setFieldError(campoCategoria, "Selecione uma categoria válida.");
      valido = false;
    }

    if (!valido) return;

    const tipo = form.querySelector('input[name="tipo"]:checked').value;

    botaoSubmit.disabled = true;
    try {
      await CFStorage.registrarMovimentacao({
        idUsuario: usuario.id,
        valor: valorNumerico,
        descricao: inputDescricao.value.trim(),
        tipo,
        idCategoria,
      });

      sessionStorage.setItem(
        "cf_toast",
        tipo === "ENTRADA" ? "Entrada registrada com sucesso." : "Saída registrada com sucesso."
      );
      window.location.href = "dashboard.html";
    } catch (erro) {
      CFValidation.showAlert(alertErro, erro.message || "Não foi possível salvar. Tente novamente.");
      botaoSubmit.disabled = false;
    }
  });
});
