/**
 * login.js — validação e autenticação da tela de login.
 *
 * Na Etapa 9, a chamada a CFStorage.autenticar() passou a ser assíncrona
 * (fetch para POST /api/auth/login), então o listener de submit agora é
 * "async" e usa "await". As mensagens de erro exibidas continuam vindo
 * das mesmas exceções do core (ValidacaoException / AutenticacaoException),
 * só que agora atravessando a rede via GlobalExceptionHandler no back-end.
 */
document.addEventListener("DOMContentLoaded", () => {
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
  const botaoSubmit = form.querySelector("button[type=submit]");

  form.addEventListener("submit", async (evento) => {
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

    botaoSubmit.disabled = true;
    try {
      const usuario = await CFStorage.autenticar(inputEmail.value.trim(), inputSenha.value);

      if (!usuario) {
        CFValidation.showAlert(alertErro, "E-mail ou senha inválidos.");
        return;
      }

      CFStorage.login(usuario);
      window.location.href = "dashboard.html";
    } catch (erro) {
      CFValidation.showAlert(alertErro, erro.message || "Não foi possível entrar. Tente novamente.");
    } finally {
      botaoSubmit.disabled = false;
    }
  });
});
