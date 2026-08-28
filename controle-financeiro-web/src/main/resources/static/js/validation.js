/**
 * validation.js
 *
 * Funções genéricas de validação e exibição de erro por campo,
 * reutilizadas pelos formulários de login, cadastro e nova movimentação.
 * As regras aqui reproduzem no front-end (por conveniência e resposta
 * imediata ao usuário) as mesmas regras já implementadas e testadas na
 * camada de negócio Java (UsuarioService, MovimentacaoService,
 * CategoriaService — Etapas 6 e 7). Esta camada de validação em JavaScript
 * NÃO substitui a validação do servidor: quando o back-end existir, ele
 * deve repetir essas checagens, pois validação apenas no cliente pode ser
 * contornada.
 */

const CFValidation = (() => {
  function isRequired(valor) {
    return typeof valor === "string" && valor.trim().length > 0;
  }

  function isEmail(valor) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor.trim());
  }

  function minLength(valor, tamanho) {
    return valor.trim().length >= tamanho;
  }

  function isPositiveNumber(valor) {
    const numero = Number(valor);
    return !Number.isNaN(numero) && numero > 0;
  }

  /** Marca um .field como inválido e exibe a mensagem associada. */
  function setFieldError(fieldEl, mensagem) {
    fieldEl.classList.add("has-error");
    const msgEl = fieldEl.querySelector(".error-message");
    if (msgEl) msgEl.textContent = mensagem;
  }

  function clearFieldError(fieldEl) {
    fieldEl.classList.remove("has-error");
  }

  function clearAllErrors(formEl) {
    formEl
      .querySelectorAll(".field.has-error")
      .forEach((field) => clearFieldError(field));
  }

  function showAlert(alertEl, mensagem) {
    alertEl.textContent = mensagem;
    alertEl.classList.add("is-visible");
  }

  function hideAlert(alertEl) {
    alertEl.classList.remove("is-visible");
  }

  return {
    isRequired,
    isEmail,
    minLength,
    isPositiveNumber,
    setFieldError,
    clearFieldError,
    clearAllErrors,
    showAlert,
    hideAlert,
  };
})();
