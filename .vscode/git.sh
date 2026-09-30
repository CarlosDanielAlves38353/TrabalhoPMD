#!/bin/bash

while true; do

    echo ""
    echo "========================================"
    echo "              GIT - PMD"
    echo "========================================"
    echo "1 - Puxar alterações do GitHub"
    echo "2 - Verificar diff"
    echo "3 - Commit e push"
    echo "4 - Ver status"
    echo "0 - Sair"
    echo "========================================"

    read -p "Escolha uma opção: " opcao

    case $opcao in

        1)
            echo ""
            echo "========================================"
            echo "       PUXAR ALTERAÇÕES DO GITHUB"
            echo "========================================"

            echo ""
            echo "Branches locais:"
            git branch

            echo ""
            read -p "Digite a branch: " branch

            if ! git show-ref --verify --quiet "refs/heads/$branch"; then
                echo "[ERRO] A branch '$branch' não existe localmente."
                continue
            fi

            echo ""
            echo "[1/2] Buscando alterações..."
            git fetch origin

            if [ $? -ne 0 ]; then
                echo "[ERRO] Falha no git fetch."
                continue
            fi

            echo ""
            echo "[2/2] Atualizando branch '$branch'..."
            git pull origin "$branch"

            if [ $? -eq 0 ]; then
                echo ""
                echo "[OK] Projeto atualizado com sucesso!"
            else
                echo ""
                echo "[ERRO] O pull falhou."
            fi
            ;;

        2)
            echo ""
            echo "========================================"
            echo "             VERIFICAR DIFF"
            echo "========================================"

            branch=$(git branch --show-current)

            if [ -z "$branch" ]; then
                echo "[ERRO] Nenhuma branch selecionada."
                continue
            fi

            echo ""
            echo "Branch atual: $branch"

            echo ""
            echo "[1/4] Atualizando referências..."
            git fetch origin

            if [ $? -ne 0 ]; then
                echo "[ERRO] Falha no git fetch."
                continue
            fi

            echo ""
            echo "========================================"
            echo "ALTERAÇÕES NÃO COMMITADAS"
            echo "========================================"

            git status --short

            echo ""
            echo "========================================"
            echo "GITHUB → LOCAL"
            echo "========================================"

            git log --oneline "HEAD..origin/$branch"

            echo ""
            echo "========================================"
            echo "LOCAL → GITHUB"
            echo "========================================"

            git log --oneline "origin/$branch..HEAD"

            echo ""
            echo "========================================"
            echo "ARQUIVOS DIFERENTES DO GITHUB"
            echo "========================================"

            git diff --name-status "origin/$branch"

            echo ""
            echo "========================================"
            echo "RESUMO"
            echo "========================================"

            behind=$(git rev-list --count "HEAD..origin/$branch")
            ahead=$(git rev-list --count "origin/$branch..HEAD")

            echo "Commits que faltam baixar: $behind"
            echo "Commits que faltam enviar: $ahead"

            if [ "$behind" -eq 0 ] && [ "$ahead" -eq 0 ]; then
                echo ""
                echo "[OK] Sua branch está sincronizada com o GitHub."
            elif [ "$behind" -gt 0 ] && [ "$ahead" -eq 0 ]; then
                echo ""
                echo "[!] Existem alterações no GitHub para baixar."
            elif [ "$behind" -eq 0 ] && [ "$ahead" -gt 0 ]; then
                echo ""
                echo "[!] Existem alterações locais para enviar."
            else
                echo ""
                echo "[!] As duas versões possuem commits diferentes."
                echo "[!] Analise antes de fazer pull ou push."
            fi
            ;;

        3)
            echo ""
            echo "========================================"
            echo "             COMMIT E PUSH"
            echo "========================================"

            echo ""
            echo "Branches locais:"
            git branch

            echo ""
            read -p "Digite a branch: " branch

            if ! git show-ref --verify --quiet "refs/heads/$branch"; then
                echo "[ERRO] A branch '$branch' não existe localmente."
                continue
            fi

            echo ""
            echo "Arquivos modificados:"
            git status --short

            echo ""
            read -p "Digite a mensagem do commit: " mensagem

            if [ -z "$mensagem" ]; then
                echo "[ERRO] A mensagem não pode estar vazia."
                continue
            fi

            echo ""
            echo "[1/3] Adicionando arquivos..."
            git add .

            if [ $? -ne 0 ]; then
                echo "[ERRO] Falha no git add."
                continue
            fi

            echo ""
            echo "[2/3] Criando commit..."
            git commit -m "$mensagem"

            if [ $? -ne 0 ]; then
                echo "[ERRO] Falha ao criar o commit."
                continue
            fi

            echo ""
            echo "[3/3] Enviando para origin/$branch..."
            git push origin "$branch"

            if [ $? -eq 0 ]; then
                echo ""
                echo "[OK] Push realizado com sucesso!"
            else
                echo ""
                echo "[ERRO] Falha ao fazer o push."
            fi
            ;;

        4)
            echo ""
            echo "========================================"
            echo "              STATUS DO GIT"
            echo "========================================"

            echo ""
            echo "Branch atual:"
            git branch --show-current

            echo ""
            echo "Status:"
            git status

            echo ""
            read -p "Pressione ENTER para continuar..."
            ;;

        0)
            echo ""
            echo "Saindo..."
            exit 0
            ;;

        *)
            echo ""
            echo "[ERRO] Opção inválida."
            ;;

    esac

    echo ""
    read -p "Pressione ENTER para voltar ao menu..."

done