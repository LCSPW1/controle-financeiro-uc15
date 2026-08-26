/**
 * nova-movimentacao.js — validação e registro de entrada/saída.
 * Espelha MovimentacaoService.registrarMovimentacao (Etapas 6/7): valor
 * numérico maior que zero, descrição obrigatória e categoria válida
 * (rejeita o placeholder "Selecione...", id 0 — mesma regra de
 * CategoriaService.buscarCategoriaValida).
 */
document.addEventListener("DOMContentLoaded", () => {
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

  // Popula o select de categorias a partir do "banco" (mesmas categorias
  // que CategoriaService.listarCategorias retornaria).
  const selectCategoria = document.getElementById("categoria");
  CFStorage.getCategorias().forEach((categoria) => {
    const option = document.createElement("option");
    option.value = String(categoria.id);
    option.textContent = categoria.nome;
    selectCategoria.appendChild(option);
  });

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

  form.addEventListener("submit", (evento) => {
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

    CFStorage.registrarMovimentacao({
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
  });
});
