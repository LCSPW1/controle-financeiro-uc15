/**
 * login.js — validação e autenticação da tela de login.
 * Espelha as regras de UsuarioService.autenticar (Etapas 6/7):
 * campos obrigatórios e credenciais válidas, lançando o equivalente
 * visual de ValidacaoException / AutenticacaoException.
 */
document.addEventListener("DOMContentLoaded", () => {
  // Se já existe sessão ativa, pula direto para o dashboard.
  if (CFStorage.getSessao()) {
    window.location.href = "dashboard.html";
    return;
  }

  const form = document.getElementById("form-login");
  const alertErro = document.getElementById("alert-erro");

  const campoEmail = document.getElementById("field-email");
  const campoSenha = document.getElementById("field-senha");
  const inputEmail = document.getElementById("email");
  const inputSenha = document.getElementById("senha");

  form.addEventListener("submit", (evento) => {
    evento.preventDefault();

    CFValidation.clearAllErrors(form);
    CFValidation.hideAlert(alertErro);

    let valido = true;

    if (!CFValidation.isRequired(inputEmail.value)) {
      CFValidation.setFieldError(campoEmail, "Informe seu e-mail.");
      valido = false;
    } else if (!CFValidation.isEmail(inputEmail.value)) {
      CFValidation.setFieldError(campoEmail, "Informe um e-mail válido.");
      valido = false;
    }

    if (!CFValidation.isRequired(inputSenha.value)) {
      CFValidation.setFieldError(campoSenha, "Informe sua senha.");
      valido = false;
    }

    if (!valido) return;

    const usuario = CFStorage.autenticar(inputEmail.value.trim(), inputSenha.value);

    if (!usuario) {
      CFValidation.showAlert(alertErro, "E-mail ou senha inválidos.");
      return;
    }

    CFStorage.login(usuario);
    window.location.href = "dashboard.html";
  });
});
