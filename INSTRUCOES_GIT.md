# Instruções — Git e GitHub (Etapa 6)

O projeto `controle-financeiro-core` já vem com um repositório Git
**inicializado localmente** (pasta `.git`, `.gitignore` e o primeiro commit
já feitos). Você só precisa criar o repositório remoto no GitHub e enviar
("push") o que já está pronto. Siga os passos abaixo, na ordem, no terminal
(cmd, PowerShell, Git Bash ou terminal do NetBeans), dentro da pasta do
projeto extraído.

## 1. Confirme que o Git está instalado

```bash
git --version
```

Se não estiver instalado, baixe em https://git-scm.com/downloads

## 2. Confirme sua identidade no Git (só precisa fazer uma vez por computador)

```bash
git config --global user.name "Seu Nome"
git config --global user.email "seu-email@exemplo.com"
```

## 3. Entre na pasta do projeto e confira o status

```bash
cd controle-financeiro-core
git status
git log --oneline
```

Você deve ver o commit inicial já criado (algo como
`"Commit inicial: refatoração da Etapa 6 - controle-financeiro-core"`).
Se não aparecer nada (repositório vazio), rode:

```bash
git add .
git commit -m "Commit inicial: refatoração da Etapa 6 - controle-financeiro-core"
```

## 4. Crie o repositório no site do GitHub

1. Acesse https://github.com e faça login.
2. Clique no botão **"+"** no canto superior direito → **"New repository"**.
3. Em **Repository name**, digite: `controle-financeiro-core`
4. Deixe como **Public** (ou Private, se sua instituição permitir e exigir
   que você adicione o professor como colaborador depois).
5. **NÃO marque** a opção "Add a README file" nem ".gitignore" nem
   "license" — o projeto já vem com esses arquivos prontos, e marcar essas
   opções pode gerar conflito na hora do push.
6. Clique em **"Create repository"**.

## 5. Conecte o repositório local ao GitHub e envie o projeto

O GitHub vai te mostrar uma URL parecida com uma destas (copie a que
aparecer para você, substituindo `SEU-USUARIO`):

```bash
# Com HTTPS (mais simples, pode pedir login/token na hora do push):
git remote add origin https://github.com/SEU-USUARIO/controle-financeiro-core.git

# Com SSH (se você já tiver chave SSH configurada no GitHub):
git remote add origin git@github.com:SEU-USUARIO/controle-financeiro-core.git
```

Depois, envie o projeto:

```bash
git branch -M main
git push -u origin main
```

Se pedir usuário e senha e a senha não funcionar: o GitHub não aceita mais
senha normal para push via HTTPS. Você precisa gerar um **Personal Access
Token** em GitHub → Settings → Developer settings → Personal access tokens
→ Generate new token, e usar esse token no lugar da senha.

## 6. Confirme que deu certo

Atualize a página do repositório no navegador. Você deve ver todas as
pastas (`src`, `nbproject`, etc.), o `README.md` renderizado embaixo da
lista de arquivos, e 1 commit no histórico.

## 7. Sempre que fizer alterações no projeto depois disso

```bash
git add .
git commit -m "Descreva o que você mudou"
git push
```

## 8. O que colocar no relatório como evidência

No relatório (`RELATORIO_ETAPA6.docx`), seção **5. Evidências do
Repositório GitHub**, insira capturas de tela de:

1. A página inicial do repositório no GitHub (mostrando a lista de
   pastas/arquivos e o README renderizado).
2. O histórico de commits (`https://github.com/SEU-USUARIO/controle-financeiro-core/commits/main`).
3. Preencha o campo "Link do repositório" com a URL pública do seu
   repositório.

## Observação importante sobre o arquivo de senha do banco

O arquivo `src/db.properties` (com a senha real do MySQL) está listado no
`.gitignore` de propósito, então **não será enviado ao GitHub** mesmo que
você o crie localmente. Isso é intencional — nunca se deve versionar
credenciais reais. Só o arquivo de exemplo (`src/db.properties.example`)
é enviado.
