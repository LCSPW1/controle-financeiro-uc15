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
