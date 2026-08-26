# Instruções — Git e GitHub

O projeto `controle-financeiro-core` já vem com um repositório Git
**inicializado localmente** (pasta `.git`, `.gitignore` e commits já
feitos). Você só precisa criar o repositório remoto no GitHub e enviar
("push") o que já está pronto. Siga os passos abaixo, na ordem, no
terminal (cmd, PowerShell, Git Bash ou terminal do NetBeans), dentro da
pasta do projeto extraído.

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

Você deve ver os commits já feitos (Etapa 6 e Etapa 7). Se não aparecer
nada (repositório vazio), rode:

```bash
git add .
git commit -m "Commit inicial"
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
pastas (`src`, `pom.xml`, `docs`, etc.), o `README.md` renderizado embaixo
da lista de arquivos, e os commits no histórico
(`https://github.com/SEU-USUARIO/controle-financeiro-core/commits/main`).

## 7. Sempre que fizer alterações no projeto depois disso

```bash
git add .
git commit -m "Descreva o que você mudou"
git push
```

## 8. Evidência de versionamento para a entrega da Etapa 7

Depois do `git push`, tire um print da página de commits do GitHub
mostrando o histórico completo (deve incluir o commit da Etapa 6 e o(s)
commit(s) da Etapa 7, incluindo a migração para Maven). Esta captura é a
"evidência do versionamento do projeto de testes" pedida no enunciado —
os testes JUnit (`src/test/java`) fazem parte do mesmo commit/repositório.

## Observação importante sobre o arquivo de senha do banco

O arquivo `src/main/resources/db.properties` (com a senha real do MySQL)
está listado no `.gitignore` de propósito, então **não será enviado ao
GitHub** mesmo que você o crie localmente. Isso é intencional — nunca se
deve versionar credenciais reais. Só o arquivo de exemplo
(`db.properties.example`) é enviado.
