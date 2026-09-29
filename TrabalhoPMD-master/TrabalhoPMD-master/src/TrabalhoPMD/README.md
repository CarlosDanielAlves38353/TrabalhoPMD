# Trabalho PMD - Plataforma de Skins

Sistema desenvolvido para o Trabalho Prático da disciplina de Programação Modular (PMD), utilizando a linguagem Java.

O software simula uma plataforma para gerenciamento de skins de diferentes jogos, permitindo cadastrar usuários, skins, avaliações e coleções pessoais.

---

## 📌 Sobre o projeto

A aplicação permite que usuários gerenciem skins de diferentes jogos, como Valorant, CS2, Fortnite e outros.

Entre as principais funcionalidades estão:

- Cadastro e gerenciamento de usuários;
- Cadastro e gerenciamento de skins;
- Associação de uma skin ao usuário que a cadastrou;
- Cadastro de avaliações de skins;
- Criação e gerenciamento de coleções;
- Adição de skins às coleções;
- Remoção de skins das coleções;
- Persistência dos dados em arquivos `txt`;
- Carregamento automático de skins através do arquivo `skins.txt`;
- Geração automática dos IDs durante o cadastro de skins.

A aplicação possui uma interface de linha de comando (CLI), através da qual o usuário pode acessar todas as funcionalidades do sistema.

---

## 🛠️ Tecnologias utilizadas

- **Java**
- Programação Orientada a Objetos
- Collections Framework (`Set`, `List`, `Map`)
- Serialização de objetos
- Arquivos `txt`
- Arquivo de dados `skins.txt`
- VS Code

---

## 📂 Estrutura do projeto

PMD/
│
├── .vscode/
│   ├── launch.json
│   └── settings.json
│
├── src/
│   └── TrabalhoPMD/
│       ├── Programa.java
│       │
│       ├── dados/
│       │   ├── BancoDados.java
│       │   └── EntidadeDAO.java
│       │
│       ├── modelo/
│       │   ├── Entidade.java
│       │   ├── Usuario.java
│       │   ├── Skin.java
│       │   ├── Avaliacao.java
│       │   ├── Colecao.java
│       │   └── ItemColecao.java
│       │
│       └── visao/
│           └── Menu.java
│
├── skins.txt
├── README.md
└── dados_*txt


https://app.notion.com/p/Skin-Collection-Documenta-o-T-cnica-3e421c8e83c380dd9785ceb8f7e38ac0?source=copy_link