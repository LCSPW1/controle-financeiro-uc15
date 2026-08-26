/**
 * cadastro.js — validação e cadastro de novo usuário.
 * Espelha UsuarioService.cadastrar (Etapas 6/7): campos obrigatórios,
 * formato de e-mail, senha com tamanho mínimo, confirmação de senha
 * (regra nova de UX, natural em um formulário web) e e-mail duplicado.
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

  form.addEventListener("submit", (evento) => {
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
    } else if (CFStorage.existeEmail(inputs.email.value.trim())) {
      CFValidation.setFieldError(campos.email, "Já existe uma conta com este e-mail.");
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

    const usuario = CFStorage.cadastrarUsuario({
      nome: inputs.nome.value.trim(),
      email: inputs.email.value.trim(),
      senha: inputs.senha.value,
    });

    CFStorage.login(usuario);
    window.location.href = "dashboard.html";
  });
});
