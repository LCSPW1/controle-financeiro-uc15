/**
 * cadastro.js — validação e cadastro de novo usuário.
 *
 * Na Etapa 8, a checagem de e-mail duplicado era feita no próprio
 * navegador (CFStorage.existeEmail, contra o localStorage). Na Etapa 9
 * isso deixou de ser possível/fazer sentido: o front-end não tem mais
 * acesso direto aos dados, então essa validação agora acontece
 * exclusivamente no servidor (UsuarioService.cadastrar, que lança
 * ValidacaoException — traduzida pelo GlobalExceptionHandler em HTTP 400).
 * O front-end apenas exibe a mensagem de erro devolvida pela API.
 */
document.addEventListener("DOMContentLoaded", () => {
  if (CFStorage.getSessao()) {
    window.location.href = "dashboard.html";
    return;
  }

  const form = document.getElementById("form-cadastro");
  const alertErro = document.getElementById("alert-erro");

  const campos = {
    nome: document.getElementById("field-nome"),
    email: document.getElementById("field-email"),
    senha: document.getElementById("field-senha"),
    confirmar: document.getElementById("field-confirmar"),
  };
  const inputs = {
    nome: document.getElementById("nome"),
    email: document.getElementById("email"),
    senha: document.getElementById("senha"),
    confirmar: document.getElementById("confirmar"),
  };
  const botaoSubmit = form.querySelector("button[type=submit]");

  form.addEventListener("submit", async (evento) => {
    evento.preventDefault();

    CFValidation.clearAllErrors(form);
    CFValidation.hideAlert(alertErro);

    let valido = true;

    if (!CFValidation.isRequired(inputs.nome.value)) {
      CFValidation.setFieldError(campos.nome, "Informe seu nome completo.");
      valido = false;
    }

    if (!CFValidation.isRequired(inputs.email.value)) {
      CFValidation.setFieldError(campos.email, "Informe seu e-mail.");
      valido = false;
    } else if (!CFValidation.isEmail(inputs.email.value)) {
      CFValidation.setFieldError(campos.email, "Informe um e-mail válido.");
      valido = false;
    }

    if (!CFValidation.isRequired(inputs.senha.value)) {
      CFValidation.setFieldError(campos.senha, "Informe uma senha.");
      valido = false;
    } else if (!CFValidation.minLength(inputs.senha.value, 6)) {
      CFValidation.setFieldError(campos.senha, "A senha precisa ter ao menos 6 caracteres.");
      valido = false;
    }

    if (!CFValidation.isRequired(inputs.confirmar.value)) {
      CFValidation.setFieldError(campos.confirmar, "Confirme sua senha.");
      valido = false;
    } else if (inputs.confirmar.value !== inputs.senha.value) {
      CFValidation.setFieldError(campos.confirmar, "As senhas não coincidem.");
      valido = false;
    }

    if (!valido) return;

    botaoSubmit.disabled = true;
    try {
      await CFStorage.cadastrarUsuario({
        nome: inputs.nome.value.trim(),
        email: inputs.email.value.trim(),
        senha: inputs.senha.value,
      });

      const usuario = await CFStorage.autenticar(inputs.email.value.trim(), inputs.senha.value);
      CFStorage.login(usuario);
      window.location.href = "dashboard.html";
    } catch (erro) {
      // Erro típico aqui: e-mail já cadastrado (HTTP 400, ValidacaoException do core)
      if (erro.status === 400) {
        CFValidation.setFieldError(campos.email, erro.message);
      } else {
        CFValidation.showAlert(alertErro, erro.message || "Não foi possível criar a conta. Tente novamente.");
      }
    } finally {
      botaoSubmit.disabled = false;
    }
  });
});
