#!/bin/bash

# Solicita os dados do usuário
read -p "Digite seu usuário do Git: " usuario
read -s -p "Digite seu Token/Senha: " senha
echo "" # Apenas pula uma linha
read -p "Digite a mensagem do commit: " mensagem

# Adiciona todos os arquivos modificados
git add .

# Faz o commit com a mensagem digitada
git commit -m "$mensagem"

# Configura a URL temporariamente com as credenciais para fazer o push
repo_url="//github.com/CarlosDanielAlves38353/TrabalhoPMD.git"

# Executa o push utilizando o usuário e senha fornecidos
git push "https://$usuario:$senha@$repo_url"