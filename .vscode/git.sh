#!/bin/bash

# ============================================================
# SCRIPT DE GERENCIAMENTO DO GIT - TRABALHO PMD
# ============================================================
# Este script cria um menu para facilitar as principais
# operações do Git utilizadas pela equipe:
#
# 1 - Puxar alterações do GitHub
# 2 - Verificar diferenças entre local e GitHub
# 3 - Fazer commit e push
# 4 - Ver status do projeto
# 0 - Sair
# ============================================================


# ------------------------------------------------------------
# while true
# ------------------------------------------------------------
# Cria um laço infinito.
# O menu continuará aparecendo até que o usuário escolha
# a opção 0, que executa "exit 0".
# ------------------------------------------------------------
while true; do

    # Limpa visualmente o espaço inicial do menu
    echo ""

    echo "========================================"
    echo "              GIT - PMD"
    echo "========================================"

    # Opções disponíveis para o usuário
    echo "1 - Puxar alterações do GitHub"
    echo "2 - Verificar diff"
    echo "3 - Commit e push"
    echo "4 - Ver status"
    echo "0 - Sair"

    echo "========================================"

    # Lê a opção digitada pelo usuário
    # e armazena na variável "opcao"
    read -p "Escolha uma opção: " opcao


    # --------------------------------------------------------
    # case
    # --------------------------------------------------------
    # O "case" verifica o valor da variável "opcao"
    # e executa o bloco correspondente.
    # --------------------------------------------------------
    case $opcao in


        # ====================================================
        # OPÇÃO 1 - PULL
        # ====================================================
        1)

            echo ""

            echo "========================================"
            echo "       PUXAR ALTERAÇÕES DO GITHUB"
            echo "========================================"

            echo ""

            # Mostra as branches existentes localmente
            echo "Branches locais:"
            git branch

            echo ""

            # Solicita ao usuário qual branch deseja atualizar
            read -p "Digite a branch: " branch


            # ------------------------------------------------
            # Verifica se a branch informada existe localmente
            # ------------------------------------------------
            # git show-ref verifica se existe uma referência
            # para essa branch.
            #
            # --verify:
            # exige que a referência exista exatamente.
            #
            # --quiet:
            # não mostra o resultado na tela.
            #
            # O ! inverte o resultado:
            # se NÃO existir, entra no if.
            # ------------------------------------------------
            if ! git show-ref --verify --quiet "refs/heads/$branch"; then

                echo "[ERRO] A branch '$branch' não existe localmente."

                # "continue" interrompe esta execução do loop
                # e volta para o menu principal.
                continue
            fi


            echo ""

            echo "[1/2] Buscando alterações..."

            # ------------------------------------------------
            # git fetch
            # ------------------------------------------------
            # Busca informações novas do repositório remoto,
            # mas não altera diretamente os arquivos locais.
            # ------------------------------------------------
            git fetch origin


            # ------------------------------------------------
            # $?
            # ------------------------------------------------
            # A variável especial "$?" guarda o código de saída
            # do último comando executado.
            #
            # 0  -> comando executado com sucesso
            # != 0 -> ocorreu algum erro
            # ------------------------------------------------
            if [ $? -ne 0 ]; then

                echo "[ERRO] Falha no git fetch."

                continue
            fi


            echo ""

            echo "[2/2] Atualizando branch '$branch'..."


            # ------------------------------------------------
            # git pull
            # ------------------------------------------------
            # Baixa as alterações do GitHub e tenta integrá-las
            # à branch local informada.
            #
            # origin = repositório remoto
            # $branch = branch escolhida pelo usuário
            # ------------------------------------------------
            git pull origin "$branch"


            # Verifica se o git pull foi executado corretamente
            if [ $? -eq 0 ]; then

                echo ""

                echo "[OK] Projeto atualizado com sucesso!"

            else

                echo ""

                echo "[ERRO] O pull falhou."

            fi

            ;;


        # ====================================================
        # OPÇÃO 2 - VERIFICAR DIFF
        # ====================================================
        2)

            echo ""

            echo "========================================"
            echo "             VERIFICAR DIFF"
            echo "========================================"


            # ------------------------------------------------
            # git branch --show-current
            # ------------------------------------------------
            # Descobre qual branch está atualmente selecionada.
            # O resultado é armazenado na variável "branch".
            # ------------------------------------------------
            branch=$(git branch --show-current)


            # Verifica se foi possível descobrir uma branch
            if [ -z "$branch" ]; then

                echo "[ERRO] Nenhuma branch selecionada."

                continue
            fi


            echo ""

            echo "Branch atual: $branch"

            echo ""

            echo "[1/4] Atualizando referências..."


            # Atualiza as referências do GitHub
            # antes de comparar com o código local.
            git fetch origin


            # Verifica se o fetch funcionou
            if [ $? -ne 0 ]; then

                echo "[ERRO] Falha no git fetch."

                continue
            fi


            echo ""

            echo "========================================"
            echo "ALTERAÇÕES NÃO COMMITADAS"
            echo "========================================"


            # ------------------------------------------------
            # git status --short
            # ------------------------------------------------
            # Mostra de forma resumida quais arquivos possuem
            # alterações locais ainda não commitadas.
            #
            # Exemplo:
            # M src/TrabalhoPMD/visao/Menu.java
            # ------------------------------------------------
            git status --short


            echo ""

            echo "========================================"
            echo "GITHUB → LOCAL"
            echo "========================================"


            # ------------------------------------------------
            # HEAD..origin/$branch
            # ------------------------------------------------
            # Mostra commits que existem no GitHub,
            # mas ainda não estão na sua branch local.
            #
            # Ou seja:
            # "O que eu ainda preciso baixar?"
            # ------------------------------------------------
            git log --oneline "HEAD..origin/$branch"


            echo ""

            echo "========================================"
            echo "LOCAL → GITHUB"
            echo "========================================"


            # ------------------------------------------------
            # origin/$branch..HEAD
            # ------------------------------------------------
            # Mostra commits que existem localmente,
            # mas ainda não foram enviados para o GitHub.
            #
            # Ou seja:
            # "O que eu ainda preciso enviar?"
            # ------------------------------------------------
            git log --oneline "origin/$branch..HEAD"


            echo ""

            echo "========================================"
            echo "ARQUIVOS DIFERENTES DO GITHUB"
            echo "========================================"


            # ------------------------------------------------
            # git diff --name-status
            # ------------------------------------------------
            # Mostra quais arquivos estão diferentes em relação
            # à versão da branch no GitHub.
            #
            # --name-status mostra apenas o nome do arquivo
            # e o tipo de alteração.
            #
            # M = modificado
            # A = adicionado
            # D = removido
            # ------------------------------------------------
            git diff --name-status "origin/$branch"


            echo ""

            echo "========================================"
            echo "RESUMO"
            echo "========================================"


            # ------------------------------------------------
            # Conta quantos commits estão faltando no local
            #
            # HEAD..origin/$branch
            # = commits existentes no GitHub e ausentes localmente
            # ------------------------------------------------
            behind=$(git rev-list --count "HEAD..origin/$branch")


            # ------------------------------------------------
            # Conta quantos commits locais ainda não foram
            # enviados para o GitHub.
            #
            # origin/$branch..HEAD
            # = commits locais ausentes no GitHub
            # ------------------------------------------------
            ahead=$(git rev-list --count "origin/$branch..HEAD")


            # Exibe as quantidades encontradas
            echo "Commits que faltam baixar: $behind"
            echo "Commits que faltam enviar: $ahead"


            # ------------------------------------------------
            # Verifica o estado da sincronização
            # ------------------------------------------------

            # Nenhum commit faltando em nenhum dos lados
            if [ "$behind" -eq 0 ] && [ "$ahead" -eq 0 ]; then

                echo ""

                echo "[OK] Sua branch está sincronizada com o GitHub."


            # Existem commits no GitHub que não estão localmente
            elif [ "$behind" -gt 0 ] && [ "$ahead" -eq 0 ]; then

                echo ""

                echo "[!] Existem alterações no GitHub para baixar."


            # Existem commits locais que ainda não foram enviados
            elif [ "$behind" -eq 0 ] && [ "$ahead" -gt 0 ]; then

                echo ""

                echo "[!] Existem alterações locais para enviar."


            # Existem alterações diferentes nos dois lados
            else

                echo ""

                echo "[!] As duas versões possuem commits diferentes."

                echo "[!] Analise antes de fazer pull ou push."

            fi

            ;;


        # ====================================================
        # OPÇÃO 3 - COMMIT E PUSH
        # ====================================================
        3)

            echo ""

            echo "========================================"
            echo "             COMMIT E PUSH"
            echo "========================================"

            echo ""

            # Mostra as branches locais
            echo "Branches locais:"
            git branch

            echo ""

            # Pergunta em qual branch o usuário deseja trabalhar
            read -p "Digite a branch: " branch


            # Verifica se a branch existe
            if ! git show-ref --verify --quiet "refs/heads/$branch"; then

                echo "[ERRO] A branch '$branch' não existe localmente."

                continue
            fi


            echo ""

            # Mostra os arquivos que possuem alterações
            echo "Arquivos modificados:"
            git status --short

            echo ""


            # Solicita a mensagem do commit
            read -p "Digite a mensagem do commit: " mensagem


            # ------------------------------------------------
            # -z
            # ------------------------------------------------
            # Verifica se a variável "mensagem" está vazia.
            # ------------------------------------------------
            if [ -z "$mensagem" ]; then

                echo "[ERRO] A mensagem não pode estar vazia."

                continue
            fi


            echo ""

            echo "[1/3] Adicionando arquivos..."


            # ------------------------------------------------
            # git add .
            # ------------------------------------------------
            # Adiciona todos os arquivos modificados e novos
            # para a área de preparação (staging).
            # ------------------------------------------------
            git add .


            # Verifica se o git add funcionou
            if [ $? -ne 0 ]; then

                echo "[ERRO] Falha no git add."

                continue
            fi


            echo ""

            echo "[2/3] Criando commit..."


            # ------------------------------------------------
            # git commit
            # ------------------------------------------------
            # Cria um commit com todos os arquivos que foram
            # adicionados ao staging.
            # ------------------------------------------------
            git commit -m "$mensagem"


            # Verifica se o commit foi criado
            if [ $? -ne 0 ]; then

                echo "[ERRO] Falha ao criar o commit."

                continue
            fi


            echo ""

            echo "[3/3] Enviando para origin/$branch..."


            # ------------------------------------------------
            # git push
            # ------------------------------------------------
            # Envia os commits locais para o repositório remoto.
            #
            # origin = nome do repositório remoto
            # $branch = branch escolhida
            # ------------------------------------------------
            git push origin "$branch"


            # Verifica se o push foi concluído
            if [ $? -eq 0 ]; then

                echo ""

                echo "[OK] Push realizado com sucesso!"

            else

                echo ""

                echo "[ERRO] Falha ao fazer o push."

            fi

            ;;


        # ====================================================
        # OPÇÃO 4 - STATUS
        # ====================================================
        4)

            echo ""

            echo "========================================"
            echo "              STATUS DO GIT"
            echo "========================================"

            echo ""

            echo "Branch atual:"


            # Mostra apenas o nome da branch atual
            git branch --show-current

            echo ""

            echo "Status:"


            # ------------------------------------------------
            # git status
            # ------------------------------------------------
            # Mostra informações completas sobre o estado
            # atual do repositório:
            #
            # - branch atual
            # - arquivos modificados
            # - arquivos no staging
            # - arquivos não rastreados
            # - situação em relação ao repositório remoto
            # ------------------------------------------------
            git status

            echo ""

            # Pausa antes de voltar ao menu
            read -p "Pressione ENTER para continuar..."

            ;;


        # ====================================================
        # OPÇÃO 0 - SAIR
        # ====================================================
        0)

            echo ""

            echo "Saindo..."


            # ------------------------------------------------
            # exit 0
            # ------------------------------------------------
            # Encerra o script.
            #
            # 0 significa que o programa terminou normalmente.
            # ------------------------------------------------
            exit 0

            ;;


        # ====================================================
        # QUALQUER OUTRA OPÇÃO
        # ====================================================
        *)

            echo ""

            echo "[ERRO] Opção inválida."

            ;;

    esac


    # --------------------------------------------------------
    # Depois de executar uma opção, pausa antes de voltar
    # para o menu principal.
    # --------------------------------------------------------
    echo ""

    read -p "Pressione ENTER para voltar ao menu..."

done
