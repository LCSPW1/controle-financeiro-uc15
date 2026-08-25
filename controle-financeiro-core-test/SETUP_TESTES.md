# Configuração do projeto de testes (controle-financeiro-core-test)

Este projeto depende de duas coisas para compilar e rodar no NetBeans:
(1) o projeto `controle-financeiro-core` (como biblioteca/jar) e
(2) a biblioteca JUnit 5. A dependência do `controle-financeiro-core` já
vem configurada no projeto; a biblioteca JUnit **pode precisar ser
adicionada manualmente**, pois o nome interno dela varia entre versões do
NetBeans.

## Passo 1 — Abra os dois projetos juntos

No NetBeans: `File → Open Project...` e selecione a pasta
`controle-financeiro-core` (projeto principal). Repita e selecione também
a subpasta `controle-financeiro-core-test` (projeto de testes). Os dois
devem aparecer na aba "Projects".

## Passo 2 — Verifique/adicione a biblioteca JUnit

1. Clique com o botão direito no projeto **controle-financeiro-core-test**
   → **Properties** → **Libraries**.
2. Vá na aba **Test Libraries** (ou "Bibliotecas de Teste").
3. Se já existir uma entrada "JUnit 5.x.x" na lista, está tudo pronto —
   pule para o Passo 3.
4. Se não existir, clique em **Add Library...** → selecione **JUnit 5.x.x**
   (ou "JUnit" caso só exista a versão 4 — nesse caso, veja a observação
   abaixo) → **Add Library**.
5. Clique em **OK**.

**Alternativa mais simples:** clique com o botão direito na pasta
`test` (Test Packages) → **New** → **JUnit Test...** e siga o assistente.
Ao criar um teste dessa forma, o próprio NetBeans detecta que falta a
biblioteca JUnit e oferece para adicioná-la automaticamente — você pode
cancelar a criação do arquivo de teste depois (os testes já vêm prontos
neste projeto) e manter apenas a biblioteca adicionada.

**Se seu NetBeans só tiver JUnit 4 disponível:** os testes deste projeto
usam anotações do JUnit 5 (`org.junit.jupiter.api.Test`). Se apenas JUnit 4
estiver disponível na sua instalação, baixe o JUnit 5 pelo Library Manager
(`Tools → Libraries → New Library...`) ou peça para o professor indicar a
versão usada na disciplina.

## Passo 3 — Verifique a dependência do projeto controle-financeiro-core

1. Clique com o botão direito no projeto **controle-financeiro-core-test**
   → **Properties** → **Libraries** → aba **Compile**.
2. Deve aparecer uma entrada referenciando o projeto
   **controle-financeiro-core** (jar). Se aparecer com um ícone de aviso
   (referência quebrada), clique com o botão direito no projeto →
   **Resolve Reference Problems...** e aponte para a pasta
   `controle-financeiro-core` (uma pasta acima).

## Passo 4 — Compile e rode os testes

1. Clique com o botão direito no projeto **controle-financeiro-core**
   (principal) → **Clean and Build** (isso gera o `.jar` que o projeto de
   testes usa como dependência).
2. Clique com o botão direito no projeto **controle-financeiro-core-test**
   → **Test** (ou o atalho Alt+F6). O NetBeans deve rodar os 4 arquivos de
   teste (`ResumoFinanceiroTest`, `MovimentacaoServiceTest`,
   `UsuarioServiceTest`, `CategoriaServiceTest`) e mostrar o resultado
   (verde = passou, vermelho = falhou) na aba "Test Results".

Se algo não compilar, copie a mensagem de erro completa e envie para
correção — este projeto foi montado fora do NetBeans (sem compilador Java
disponível no ambiente de montagem), então é importante validar a primeira
compilação real no seu computador.
