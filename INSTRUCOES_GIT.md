# Instruções — Novo repositório Git/GitHub (Etapa 9)

O enunciado desta etapa pede explicitamente um **novo repositório**
("Aplique versionamento ao projeto, criando novo repositório") — diferente
da Etapa 7, que continuou o repositório da Etapa 6. Por isso, este projeto
já vem com um **repositório Git novo e independente**, com 1 commit
inicial contendo a integração completa (core + web + front-end).

## 1. Confirme o estado do repositório local

```bash
cd controle-financeiro-webapp
git log --oneline
git status
```

Você deve ver 1 commit: "Etapa 9: integração completa - back-end Spring
REST + banco de dados + front-end".

## 2. Crie o repositório no GitHub

1. Acesse https://github.com e faça login.
2. **"+"** → **"New repository"**.
3. Nome sugerido: `controle-financeiro-webapp`.
4. **NÃO marque** README/.gitignore/license (o projeto já tem os seus).
5. **Create repository**.

## 3. Conecte e envie

```bash
git remote add origin https://github.com/SEU-USUARIO/controle-financeiro-webapp.git
git push -u origin main
```

(Se pedir senha e não funcionar, use um Personal Access Token — GitHub não
aceita mais senha comum para push via HTTPS. Veja GitHub → Settings →
Developer settings → Personal access tokens.)

## 4. Evidência de versionamento para a entrega

Depois do push, tire prints de:

1. **Página inicial do repositório** — mostrando a estrutura de pastas
   (`controle-financeiro-core/`, `controle-financeiro-web/`, `docs/`,
   `pom.xml`, `README.md`) e o README renderizado.
2. **Histórico de commits** (`.../commits/main`) — mesmo tendo só 1
   commit nesta entrega, ele mostra no diff a integração completa; se
   você fizer commits adicionais depois de testar/corrigir algo, eles
   também devem aparecer aqui.
3. Cole o link do repositório no documento
   `docs/EVIDENCIAS_VERSIONAMENTO.docx`, no espaço reservado.

## 5. Se você for continuar trabalhando no projeto depois

```bash
git add .
git commit -m "Descreva o que você mudou"
git push
```

Sugestão: se ao rodar os testes do plano da Etapa 7 contra a aplicação
real você encontrar algum problema e corrigi-lo, faça um commit específico
para essa correção (ex.: "Corrige X encontrado durante testes manuais") —
isso enriquece o histórico como evidência de bugtracking real, além do
documento `docs/EVIDENCIAS_TESTES_E_BUGTRACKING.docx`.

## Observação sobre credenciais

Nenhuma senha real de banco de dados está neste repositório — o perfil
MySQL (`application-mysql.properties`) tem apenas um valor de exemplo
(`CHANGE_ME`) que você deve substituir localmente, sem commitar a senha
real. O `.gitignore` já bloqueia `application-local.properties` e
`db.properties`, caso você crie algum desses para guardar credenciais
reais.
