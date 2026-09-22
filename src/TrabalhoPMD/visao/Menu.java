package TrabalhoPMD.visao;

import TrabalhoPMD.dados.BancoDados;
import TrabalhoPMD.dados.EntidadeDAO;
import TrabalhoPMD.modelo.Avaliacao;
import TrabalhoPMD.modelo.Colecao;
import TrabalhoPMD.modelo.Entidade;
import TrabalhoPMD.modelo.ItemColecao;
import TrabalhoPMD.modelo.Perfil;
import TrabalhoPMD.modelo.Skin;
import TrabalhoPMD.modelo.Usuario;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Menu {

    // =====================================================
    // ATRIBUTOS
    // =====================================================
    private Scanner scanner;
    private BancoDados banco;
    private Usuario usuarioLogado;

    // =====================================================
    // CONSTRUTOR
    // =====================================================
    public Menu() {
        scanner = new Scanner(System.in);
        banco = BancoDados.getInstancia();
    }

    // =====================================================
    // EXECUCAO
    // =====================================================
    public void executar() {

        banco.recuperarTodos();
        carregarSkinsTxt();

        menuInicial();

        banco.persistirTodos();

        scanner.close();
    }

    // =====================================================
    // LOGIN E CADASTRO
    // =====================================================
    private void menuInicial() {
        int opcao;

        do {
            System.out.println();
            System.out.println("=================================");
            System.out.println("         SKIN COLLECTION");
            System.out.println("=================================");
            System.out.println("1 - Entrar");
            System.out.println("2 - Criar conta");
            System.out.println("0 - Sair");
            System.out.println("=================================");
            System.out.print("Opcao: ");

            opcao = lerInt();

            switch (opcao) {
                case 1:
                    if (fazerLogin()) {
                        if (usuarioLogado.getPerfil() == TrabalhoPMD.modelo.Perfil.ADMIN) {

                            menuPrincipalAdmin();

                        } else {
                            menuPrincipalUsuario();
                        }
                    }
                    break;

                case 2:
                    criarConta();
                    break;

                case 0:
                    System.out.println();
                    System.out.println("Programa encerrado.");
                    break;

                default:
                    System.out.println("Opcao invalida.");
            }

        } while (opcao != 0);
    }

    private boolean fazerLogin() {

        EntidadeDAO daoUsuario = banco.getDAO(Usuario.class);

        System.out.println();
        System.out.println("===== LOGIN =====");

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Entidade[] usuarios = daoUsuario.carregar();

        for (Entidade entidade : usuarios) {

            Usuario usuario = (Usuario) entidade;

            if (usuario.getEmail().equalsIgnoreCase(email)
                    && usuario.getSenha().equals(senha)) {

                usuarioLogado = usuario;

                System.out.println();
                System.out.println("[OK] Login realizado com sucesso!");
                System.out.println("Bem-vindo, " + usuario.getNome() + "!");
                System.out.println("Perfil: " + usuario.getPerfil());

                return true;
            }
        }

        System.out.println();
        System.out.println("[ERRO] E-mail ou senha incorretos.");

        return false;
    }

    private void criarConta() {
        System.out.println();
        System.out.println("===== CRIAR CONTA =====");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("E-mail: ");
        String email = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        // Verifica se o e-mail já está cadastrado
        EntidadeDAO daoUsuario = banco.getDAO(Usuario.class);

        for (Entidade entidade : daoUsuario.carregar()) {
            Usuario usuario = (Usuario) entidade;

            if (usuario.getEmail().equalsIgnoreCase(email)) {
                System.out.println();
                System.out.println("[ERRO] Este e-mail já está cadastrado.");
                return;
            }
        }

        int id = proximoId(Usuario.class);

        Usuario usuario = new Usuario(
                id,
                nome,
                email,
                senha,
                TrabalhoPMD.modelo.Perfil.USUARIO);

        if (daoUsuario.salvar(usuario)) {
            System.out.println();
            System.out.println("[OK] Conta criada com sucesso!");
            System.out.println("Seu ID: " + id);
            System.out.println("Agora você já pode fazer login.");
        } else {
            System.out.println();
            System.out.println("[ERRO] Não foi possível criar a conta.");
        }
    }

    // =====================================================
    // MENUS PRINCIPAIS
    // =====================================================
    private void menuPrincipalUsuario() {

        while (true) {

            System.out.println();
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║          SKIN COLLECTION             ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║ Olá, " + usuarioLogado.getNome());
            System.out.println("║ Perfil: " + usuarioLogado.getPerfil());
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║ 1 - Explorar skins                   ║");
            System.out.println("║ 2 - Pesquisar skin                    ║");
            System.out.println("║ 3 - Minha coleção                    ║");
            System.out.println("║ 4 - Meu perfil                       ║");
            System.out.println("║ 0 - Logout                           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Escolha: ");

            int opcao = lerInt();

            switch (opcao) {

                case 1:
                    visualizarSkinsPorJogo();
                    break;

                case 2:
                    pesquisarSkin();
                    break;

                case 3:
                    menuMinhaColecao();
                    break;

                case 4:
                    System.out.println(usuarioLogado);
                    break;

                case 0:
                    usuarioLogado = null;
                    System.out.println("Logout realizado.");
                    return;

                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private void menuPrincipalAdmin() {
        int opcao;

        do {
            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║          PAINEL ADMINISTRATIVO         ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ 1 - Gerenciar usuários                 ║");
            System.out.println("║ 2 - Gerenciar skins                    ║");
            System.out.println("║ 3 - Gerenciar avaliações               ║");
            System.out.println("║ 4 - Gerenciar coleções                 ║");
            System.out.println("║ 5 - Explorar skins por jogo            ║");
            System.out.println("║ 6 - Criar administrador                ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ 0 - Sair                               ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.print("Escolha uma opção: ");

            opcao = lerInt("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    menuUsuario();
                    break;

                case 2:
                    menuSkin();
                    break;

                case 3:
                    menuAvaliacao();
                    break;

                case 4:
                    menuColecao();
                    break;

                case 5:
                    visualizarSkinsPorJogo();
                    break;

                case 6:
                    criarAdministrador();
                    break;

                case 0:
                    usuarioLogado = null;
                    System.out.println("Logout realizado.");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    // =====================================================
    // FUNCIONALIDADES DO USUARIO
    // =====================================================

    // =============================
    // MENU COLEÇÃO
    // ==============================
    private void menuMinhaColecao() {

        if (usuarioLogado == null) {
            System.out.println("[ERRO] Nenhum usuário está logado.");
            return;
        }

        Colecao colecao = usuarioLogado.getColecao();

        // Se o usuário ainda não possui coleção, cria uma
        if (colecao == null) {

            int id = proximoId(Colecao.class);

            colecao = new Colecao(
                    id,
                    usuarioLogado,
                    "Minha Coleção");

            usuarioLogado.setColecao(colecao);
            banco.getDAO(Colecao.class).salvar(colecao);
        }

        int opcao;

        do {

            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║             MINHA COLEÇÃO              ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ 1 - Visualizar skins                   ║");
            System.out.println("║ 2 - Adicionar skin                     ║");
            System.out.println("║ 3 - Remover skin                       ║");
            System.out.println("║ 0 - Voltar                             ║");
            System.out.println("╚════════════════════════════════════════╝");

            System.out.print("Escolha: ");
            opcao = lerInt();

            switch (opcao) {

                case 1:
                    visualizarMinhaColecao(colecao);
                    break;

                case 2:
                    adicionarSkinColecao(colecao);
                    break;

                case 3:
                    removerSkinColecao(colecao);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("[ERRO] Opção inválida.");
            }

        } while (opcao != 0);
    }

    // =============================
    // VISUALIZAR COLEÇÃO
    // ==============================
    private void visualizarMinhaColecao(Colecao colecao) {

        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║             MINHAS SKINS               ║");
        System.out.println("╚════════════════════════════════════════╝");

        if (colecao.getItens().isEmpty()) {

            System.out.println();
            System.out.println("[INFO] Sua coleção está vazia.");
            return;
        }

        int total = 0;

        for (ItemColecao item : colecao.getItens()) {

            Skin skin = item.getSkin();

            System.out.println();
            System.out.println("┌────────────────────────────────────────┐");
            System.out.printf(
                    "│ #%d - %-34s │%n",
                    skin.getId(),
                    limitarTexto(skin.getNome(), 34));
            System.out.println("├────────────────────────────────────────┤");
            System.out.printf(
                    "│ Jogo:      %-28s │%n",
                    limitarTexto(skin.getJogo(), 28));
            System.out.printf(
                    "│ Raridade:  %-28s │%n",
                    limitarTexto(skin.getRaridade(), 28));
            System.out.printf(
                    "│ Quantidade: %-27d │%n",
                    item.getQuantidade());
            System.out.println("└────────────────────────────────────────┘");

            total += item.getQuantidade();
        }

        System.out.println();
        System.out.println("Total de skins: " + total);
    }

    // =============================
    // ADICIONAR SKIN COLEÇÃO
    // ==============================
    private void adicionarSkinColecao(Colecao colecao) {

        System.out.println();
        System.out.println("===== ADICIONAR SKIN =====");

        System.out.println();
        System.out.println("===== SKINS DISPONÍVEIS =====");

        Entidade[] skins = dao(Skin.class).carregar();

        if (skins.length == 0) {
            System.out.println("Nenhuma skin cadastrada.");
            return;
        }

        for (Entidade entidade : skins) {
            Skin skin = (Skin) entidade;

            System.out.println(
                    "ID: " + skin.getId()
                            + " | " + skin.getNome()
                            + " | " + skin.getJogo()
                            + " | " + skin.getRaridade());
        }

        System.out.println();

        int idSkin = lerInt("ID da skin: ");

        Entidade entidade = dao(Skin.class).buscar(idSkin);

        if (entidade == null) {
            System.out.println("[ERRO] Skin não encontrada.");
            return;
        }

        Skin skin = (Skin) entidade;

        int quantidade = lerInt("Quantidade: ");

        if (quantidade <= 0) {
            System.out.println("[ERRO] A quantidade deve ser maior que zero.");
            return;
        }

        colecao.adicionarItem(skin, quantidade);

        banco.getDAO(Colecao.class).atualizar(colecao);

        System.out.println();
        System.out.println("[OK] Skin adicionada à sua coleção!");
    }

    // =============================
    // REMOVER SKIN COLEÇÃO
    // ==============================
    private void removerSkinColecao(Colecao colecao) {

        if (colecao.getItens().isEmpty()) {
            System.out.println();
            System.out.println("[INFO] Sua coleção está vazia.");
            return;
        }

        visualizarMinhaColecao(colecao);

        int idSkin = lerInt("ID da skin que deseja remover: ");

        if (colecao.removerItem(idSkin)) {

            banco.getDAO(Colecao.class).atualizar(colecao);

            System.out.println();
            System.out.println("[OK] Skin removida da sua coleção.");

        } else {

            System.out.println();
            System.out.println("[ERRO] Essa skin não está na sua coleção.");
        }
    }

    private void ordenarSkins() {
        EntidadeDAO daoSkin = banco.getDAO(Skin.class);

        Entidade[] entidades = daoSkin.carregar();

        if (entidades.length == 0) {
            System.out.println("[!] Nenhuma skin cadastrada.");
            return;
        }

        Skin[] skins = new Skin[entidades.length];

        for (int i = 0; i < entidades.length; i++) {
            skins[i] = (Skin) entidades[i];
        }

        System.out.println();
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║             ORDENAR SKINS              ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1 - Nome A-Z                           ║");
        System.out.println("║ 2 - Nome Z-A                           ║");
        System.out.println("║ 3 - Por jogo                           ║");
        System.out.println("║ 4 - Por raridade                       ║");
        System.out.println("║ 5 - Maior avaliação                    ║");
        System.out.println("║ 6 - Menor avaliação                    ║");
        System.out.println("║ 7 - Pesquisar por nome                 ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 0 - Voltar                             ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("Escolha uma opção: ");

        int opcao = lerInt("Escolha uma opção: ");

        if (opcao == 0) {
            return;
        }

        if (opcao < 1 || opcao > 7) {
            System.out.println("[ERRO] Opção inválida.");
            return;
        }

        for (int i = 0; i < skins.length - 1; i++) {
            for (int j = 0; j < skins.length - 1 - i; j++) {

                boolean trocar = false;

                switch (opcao) {

                    case 1:
                        trocar = compararTexto(
                                skins[j].getNome(),
                                skins[j + 1].getNome()) > 0;
                        break;

                    case 2:
                        trocar = compararTexto(
                                skins[j].getNome(),
                                skins[j + 1].getNome()) < 0;
                        break;

                    case 3:
                        visualizarSkinsPorJogo();
                        break;

                    case 4:
                        trocar = compararTexto(
                                skins[j].getRaridade(),
                                skins[j + 1].getRaridade()) > 0;
                        break;

                    case 5:
                        trocar = calcularMedia(skins[j]) < calcularMedia(skins[j + 1]);
                        break;

                    case 6:
                        trocar = calcularMedia(skins[j]) > calcularMedia(skins[j + 1]);
                        break;

                    case 7:
                        pesquisarSkin();
                        break;
                }

                if (trocar) {
                    Skin temp = skins[j];
                    skins[j] = skins[j + 1];
                    skins[j + 1] = temp;
                }
            }
        }

        exibirSkinsOrdenadas(skins, opcao);
    }

    private void exibirSkinsOrdenadas(Skin[] skins, int opcao) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("          SKINS ORDENADAS");
        System.out.println("========================================");

        for (Skin skin : skins) {
            double media = calcularMedia(skin);

            System.out.println();
            System.out.println("┌──────────────────────────────────────────────┐");
            System.out.println("│ Nome: " + skin.getNome());
            System.out.println("│ Jogo: " + skin.getJogo());
            System.out.println("│ Raridade: " + skin.getRaridade());
            System.out.printf("│ Avaliação média: %.1f%n", media);
            System.out.println("└──────────────────────────────────────────────┘");
        }

        System.out.println();
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    // ==========================================
    // MENU DE ADMINISTRAÇÃO
    // ==========================================

    private void menuUsuario() {

        int opcao;

        do {
            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║               USUÁRIOS                 ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ 1 - Inserir                            ║");
            System.out.println("║ 2 - Alterar                            ║");
            System.out.println("║ 3 - Apagar                             ║");
            System.out.println("║ 4 - Visualizar por ID                  ║");
            System.out.println("║ 5 - Visualizar todos                   ║");
            System.out.println("║ 0 - Voltar                             ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.print("Escolha uma opção: ");

            opcao = lerInt();

            switch (opcao) {

                case 1:
                    inserirUsuario();
                    break;

                case 2:
                    alterarUsuario();
                    break;

                case 3:
                    apagar(Usuario.class);
                    break;

                case 4:
                    visualizarPorId(Usuario.class);
                    break;

                case 5:
                    visualizarTodos(Usuario.class);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcao invalida.");
            }

        } while (opcao != 0);
    }

    // ==========================================
    // MENU DE SKIN ADMINISTRAÇÃO
    // ==========================================

    private void menuSkin() {

        int opcao;

        do {

            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║               SKINS                    ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ 1 - Inserir                            ║");
            System.out.println("║ 2 - Alterar                            ║");
            System.out.println("║ 3 - Apagar                             ║");
            System.out.println("║ 4 - Visualizar por ID                  ║");
            System.out.println("║ 5 - Visualizar todos                   ║");
            System.out.println("║ 6 - Ordenar skins                      ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ 0 - Voltar                             ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.print("Escolha uma opção: ");

            opcao = lerInt();

            switch (opcao) {

                case 1:
                    inserirSkin();
                    break;

                case 2:
                    alterarSkin();
                    break;

                case 3:
                    apagar(Skin.class);
                    break;

                case 4:
                    visualizarPorId(Skin.class);
                    break;

                case 5:
                    visualizarTodos(Skin.class);
                    break;

                case 6:
                    ordenarSkins();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcao invalida.");
            }

        } while (opcao != 0);
    }

    // ==========================================
    // MENU DE AVALIACAO
    // ==========================================

    private void menuAvaliacao() {

        int opcao;

        do {

            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║             AVALIAÇÕES                 ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ 1 - Inserir                            ║");
            System.out.println("║ 2 - Alterar                            ║");
            System.out.println("║ 3 - Apagar                             ║");
            System.out.println("║ 4 - Visualizar por ID                  ║");
            System.out.println("║ 5 - Visualizar todos                   ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ 0 - Voltar                             ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.print("Escolha uma opção: ");
            opcao = lerInt();

            switch (opcao) {

                case 1:
                    inserirAvaliacao();
                    break;

                case 2:
                    alterarAvaliacao();
                    break;

                case 3:
                    apagar(Avaliacao.class);
                    break;

                case 4:
                    visualizarPorId(Avaliacao.class);
                    break;

                case 5:
                    visualizarTodos(Avaliacao.class);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcao invalida.");
            }

        } while (opcao != 0);
    }

    // ==========================================
    // MENU DE COLECAO
    // ==========================================

    private void menuColecao() {

        int opcao;

        do {

            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║              COLEÇÕES                  ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ 1 - Inserir                            ║");
            System.out.println("║ 2 - Alterar                            ║");
            System.out.println("║ 3 - Apagar                             ║");
            System.out.println("║ 4 - Visualizar por ID                  ║");
            System.out.println("║ 5 - Visualizar todos                   ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║ 0 - Voltar                             ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.print("Escolha uma opção: ");

            opcao = lerInt();

            switch (opcao) {

                case 1:
                    inserirColecao();
                    break;

                case 2:
                    alterarColecao();
                    break;

                case 3:
                    apagar(Colecao.class);
                    break;

                case 4:
                    visualizarPorId(Colecao.class);
                    break;

                case 5:
                    visualizarTodos(Colecao.class);
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opcao invalida.");
            }

        } while (opcao != 0);
    }

    private void criarAdministrador() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("         CRIAR ADMINISTRADOR");
        System.out.println("========================================");

        EntidadeDAO daoUsuario = banco.getDAO(Usuario.class);

        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        // Verifica se o e-mail já está cadastrado
        Entidade[] usuarios = daoUsuario.carregar();

        for (Entidade entidade : usuarios) {
            Usuario usuario = (Usuario) entidade;

            if (usuario.getEmail().equalsIgnoreCase(email)) {
                System.out.println("[ERRO] Já existe um usuário com esse e-mail.");
                return;
            }
        }

        int id = proximoId(Usuario.class);

        Usuario administrador = new Usuario(
                id,
                nome,
                email,
                senha,
                Perfil.ADMIN);

        if (daoUsuario.salvar(administrador)) {
            daoUsuario.persistir();

            System.out.println();
            System.out.println("[OK] Administrador criado com sucesso!");
            System.out.println("ID: " + id);
            System.out.println("Nome: " + nome);
            System.out.println("E-mail: " + email);
        } else {
            System.out.println("[ERRO] Não foi possível criar o administrador.");
        }
    }

    // =====================================================
    // CRUD - USUARIO
    // =====================================================

    private void inserirUsuario() {

        System.out.println();
        System.out.println("Novo usuario");

        int id = lerInt("ID: ");

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Usuario usuario = new Usuario(id, nome, email, senha);

        if (dao(Usuario.class).salvar(usuario)) {

            System.out.println("Usuario salvo.");

        } else {

            System.out.println(
                    "Nao foi possivel salvar o usuario.");
        }
    }

    private void alterarUsuario() {

        int id = lerInt("ID do usuario: ");

        Usuario usuario = (Usuario) dao(Usuario.class).buscar(id);

        if (usuario == null) {

            System.out.println(
                    "Usuario nao encontrado.");

            return;
        }

        System.out.print("Novo nome: ");
        usuario.setNome(scanner.nextLine());

        System.out.print("Novo email: ");
        usuario.setEmail(scanner.nextLine());

        System.out.print("Nova senha: ");
        usuario.setSenha(scanner.nextLine());

        dao(Usuario.class).atualizar(usuario);

        System.out.println(
                "Usuario atualizado.");
    }

    // =====================================================
    // CRUD - SKIN
    // =====================================================

    private void inserirSkin() {

        EntidadeDAO daoSkin = banco.getDAO(Skin.class);
        EntidadeDAO daoUsuario = banco.getDAO(Usuario.class);

        int id = proximoId(Skin.class);

        System.out.println();
        System.out.println("===== INSERIR SKIN =====");
        System.out.println("ID gerado automaticamente: " + id);

        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Jogo: ");
        String jogo = scanner.nextLine();

        System.out.print("Raridade: ");
        String raridade = scanner.nextLine();

        Usuario usuario = null;

        System.out.println();
        System.out.println("Quem cadastrou essa skin?");
        System.out.println("1 - Usuario");
        System.out.println("2 - Ninguem");
        System.out.print("Opcao: ");

        int opcao = lerInt();

        if (opcao == 1) {

            System.out.print("Digite o ID do usuario: ");
            int idUsuario = lerInt();

            Entidade entidadeUsuario = daoUsuario.buscar(idUsuario);

            if (entidadeUsuario == null) {

                System.out.println(
                        "Usuario nao encontrado.");

                return;
            }

            usuario = (Usuario) entidadeUsuario;

        } else if (opcao != 2) {

            System.out.println(
                    "Opcao invalida.");

            return;
        }

        Skin skin = new Skin(
                id,
                nome,
                jogo,
                raridade,
                usuario);

        if (daoSkin.salvar(skin)) {

            daoSkin.persistir();

            System.out.println();
            System.out.println(
                    "Skin cadastrada com sucesso!");

        } else {

            System.out.println();
            System.out.println(
                    "Erro ao cadastrar a skin.");
        }
    }

    private void alterarSkin() {

        int id = lerInt("ID da skin: ");

        Skin skin = (Skin) dao(Skin.class).buscar(id);

        if (skin == null) {

            System.out.println(
                    "Skin nao encontrada.");

            return;
        }

        System.out.print("Novo nome: ");
        skin.setNome(scanner.nextLine());

        System.out.print("Novo jogo: ");
        skin.setJogo(scanner.nextLine());

        System.out.print("Nova raridade: ");
        skin.setRaridade(scanner.nextLine());

        dao(Skin.class).atualizar(skin);

        System.out.println(
                "Skin atualizada.");
    }

    private void visualizarSkinsPorJogo() {

        EntidadeDAO dao = banco.getDAO(Skin.class);
        Entidade[] entidades = dao.carregar();

        java.util.List<String> jogos = new java.util.ArrayList<>();

        // Descobre os jogos existentes
        for (Entidade entidade : entidades) {
            Skin skin = (Skin) entidade;

            if (!jogos.contains(skin.getJogo())) {
                jogos.add(skin.getJogo());
            }
        }

        // Ordena os jogos em ordem alfabética
        java.util.Collections.sort(jogos);

        int opcao = -1;

        while (opcao != 0) {

            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.println("║          EXPLORAR SKINS                ║");
            System.out.println("╠════════════════════════════════════════╣");

            for (int i = 0; i < jogos.size(); i++) {
                System.out.printf(
                        "║ %2d - %-32s ║%n",
                        i + 1,
                        jogos.get(i));
            }

            System.out.println("╠════════════════════════════════════════╣");
            System.out.println("║  0 - Voltar                            ║");
            System.out.println("╚════════════════════════════════════════╝");

            opcao = lerInt("Escolha: ");

            // Se escolher 0, o while termina
            if (opcao == 0) {
                break;
            }

            if (opcao < 1 || opcao > jogos.size()) {
                System.out.println();
                System.out.println("[ERRO] Opção inválida.");
                continue;
            }

            String jogoSelecionado = jogos.get(opcao - 1);

            System.out.println();
            System.out.println("╔════════════════════════════════════════╗");
            System.out.printf(
                    "║        %-32s ║%n",
                    jogoSelecionado.toUpperCase());
            System.out.println("╚════════════════════════════════════════╝");

            boolean encontrou = false;
            int contador = 0;

            for (Entidade entidade : entidades) {

                Skin skin = (Skin) entidade;

                if (skin.getJogo().equalsIgnoreCase(jogoSelecionado)) {

                    contador++;

                    System.out.println();
                    System.out.println("┌────────────────────────────────────────┐");

                    System.out.printf(
                            "│ #%d - %-34s │%n",
                            skin.getId(),
                            limitarTexto(skin.getNome(), 34));

                    System.out.println("├────────────────────────────────────────┤");

                    System.out.printf(
                            "│ Jogo:     %-29s │%n",
                            limitarTexto(skin.getJogo(), 29));

                    System.out.printf(
                            "│ Raridade: %-29s │%n",
                            limitarTexto(skin.getRaridade(), 29));

                    System.out.printf(
                            "│ Avaliação: ★ %.1f / 10.0               │%n",
                            calcularMedia(skin));

                    System.out.println("└────────────────────────────────────────┘");

                    encontrou = true;
                }
            }

            if (!encontrou) {

                System.out.println();

                System.out.println(
                        "[INFO] Nenhuma skin encontrada para esse jogo.");

            } else {

                System.out.println();

                System.out.println(
                        "Total de skins encontradas: " + contador);
            }

            System.out.println();

            System.out.println("Pressione ENTER para continuar...");
            scanner.nextLine();
            if (!encontrou) {

                System.out.println();

                System.out.println(
                        "[INFO] Nenhuma skin encontrada para esse jogo.");

                System.out.println();
                System.out.println("Pressione ENTER para continuar...");
                scanner.nextLine();

            } else {

                System.out.println();

                System.out.println(
                        "Total de skins encontradas: " + contador);

                System.out.println();

                System.out.println("╔════════════════════════════════════════╗");
                System.out.println("║              AÇÕES                    ║");
                System.out.println("╠════════════════════════════════════════╣");
                System.out.println("║ 1 - Ver detalhes de uma skin          ║");
                System.out.println("║ 2 - Avaliar uma skin                  ║");
                System.out.println("║ 3 - Ver avaliações                    ║");
                System.out.println("║ 0 - Voltar                             ║");
                System.out.println("╚════════════════════════════════════════╝");

                int acao = lerInt("Escolha uma opção: ");

                if (acao == 1) {

                    int idSkin = lerInt("Digite o ID da skin: ");

                    Skin skinSelecionada = (Skin) dao(Skin.class).buscar(idSkin);

                    if (skinSelecionada != null
                            && skinSelecionada.getJogo()
                                    .equalsIgnoreCase(jogoSelecionado)) {

                        visualizarDetalhesSkin(skinSelecionada);

                    } else {

                        System.out.println("[ERRO] Skin não encontrada nesse jogo.");
                    }

                } else if (acao == 2) {

                    int idSkin = lerInt("Digite o ID da skin: ");

                    Skin skinSelecionada = (Skin) dao(Skin.class).buscar(idSkin);

                    if (skinSelecionada != null
                            && skinSelecionada.getJogo()
                                    .equalsIgnoreCase(jogoSelecionado)) {

                        avaliarSkin(skinSelecionada);

                    } else {

                        System.out.println("[ERRO] Skin não encontrada nesse jogo.");
                    }

                } else if (acao == 3) {

                    int idSkin = lerInt("Digite o ID da skin: ");

                    Skin skinSelecionada = (Skin) dao(Skin.class).buscar(idSkin);

                    if (skinSelecionada != null
                            && skinSelecionada.getJogo()
                                    .equalsIgnoreCase(jogoSelecionado)) {

                        visualizarAvaliacoesDaSkin(skinSelecionada);

                    } else {

                        System.out.println("[ERRO] Skin não encontrada nesse jogo.");
                    }

                } else if (acao != 0) {

                    System.out.println("[ERRO] Opção inválida.");
                }
            }
        }
    }

    private void visualizarDetalhesSkin(Skin skin) {

        System.out.println();

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║             DETALHES DA SKIN           ║");
        System.out.println("╠════════════════════════════════════════╣");

        System.out.printf(
                "║ ID: %-34d ║%n",
                skin.getId());

        System.out.printf(
                "║ Nome: %-32s ║%n",
                limitarTexto(skin.getNome(), 32));

        System.out.printf(
                "║ Jogo: %-32s ║%n",
                limitarTexto(skin.getJogo(), 32));

        System.out.printf(
                "║ Raridade: %-27s ║%n",
                limitarTexto(skin.getRaridade(), 27));

        System.out.printf(
                "║ Avaliação: ★ %.1f / 10.0             ║%n",
                calcularMedia(skin));

        System.out.printf(
                "║ Avaliações: %-26d ║%n",
                contarAvaliacoes(skin));

        System.out.println("╚════════════════════════════════════════╝");

        System.out.println();
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    private void avaliarSkin(Skin skin) {

        System.out.println();

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║              AVALIAR SKIN              ║");
        System.out.println("╠════════════════════════════════════════╣");

        System.out.printf(
                "║ Skin: %-31s ║%n",
                limitarTexto(skin.getNome(), 31));

        System.out.println("╚════════════════════════════════════════╝");

        int nota;

        do {

            nota = lerInt("Nota (1 a 10): ");

            if (nota < 1 || nota > 10) {

                System.out.println(
                        "[ERRO] A nota deve estar entre 1 e 10.");
            }

        } while (nota < 1 || nota > 10);

        System.out.print("Comentário: ");
        String comentario = scanner.nextLine();

        int id = proximoId(Avaliacao.class);

        Avaliacao avaliacao = new Avaliacao(
                id,
                usuarioLogado,
                skin,
                nota,
                comentario);

        EntidadeDAO daoAvaliacao = banco.getDAO(Avaliacao.class);

        if (daoAvaliacao.salvar(avaliacao)) {

            daoAvaliacao.persistir();

            System.out.println();
            System.out.println("[OK] Avaliação registrada!");
            System.out.println(
                    "Sua nota: " + gerarEstrelas(nota)
                            + " (" + nota + "/10)");

        } else {

            System.out.println();
            System.out.println(
                    "[ERRO] Não foi possível registrar a avaliação.");
        }
    }

    private String gerarEstrelas(int nota) {

        int estrelas = (int) Math.ceil(nota / 2.0);

        StringBuilder resultado = new StringBuilder();

        for (int i = 1; i <= 5; i++) {

            if (i <= estrelas) {
                resultado.append("★");
            } else {
                resultado.append("☆");
            }
        }

        return resultado.toString();
    }

    private void visualizarAvaliacoesDaSkin(Skin skin) {

        EntidadeDAO daoAvaliacao = banco.getDAO(Avaliacao.class);

        Entidade[] avaliacoes = daoAvaliacao.carregar();

        System.out.println();

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║             AVALIAÇÕES                 ║");
        System.out.println("╠════════════════════════════════════════╣");

        System.out.printf(
                "║ Skin: %-31s ║%n",
                limitarTexto(skin.getNome(), 31));

        System.out.printf(
                "║ Média: ★ %.1f / 10.0                 ║%n",
                calcularMedia(skin));

        System.out.println("╚════════════════════════════════════════╝");

        boolean encontrou = false;

        for (Entidade entidade : avaliacoes) {

            Avaliacao avaliacao = (Avaliacao) entidade;

            if (avaliacao.getSkin() != null
                    && avaliacao.getSkin().getId() == skin.getId()) {

                encontrou = true;

                String nomeUsuario = avaliacao.getUsuario() == null
                        ? "Usuário desconhecido"
                        : avaliacao.getUsuario().getNome();

                String comentario = avaliacao.getComentario() == null
                        || avaliacao.getComentario().isEmpty()
                                ? "Sem comentário"
                                : avaliacao.getComentario();

                System.out.println();

                System.out.println(
                        "┌────────────────────────────────────────┐");

                System.out.printf(
                        "│ %-38s │%n",
                        limitarTexto(nomeUsuario, 38));

                System.out.printf(
                        "│ %s  (%d/10)%s │%n",
                        gerarEstrelas(avaliacao.getNota()),
                        avaliacao.getNota(),
                        " ".repeat(
                                Math.max(
                                        0,
                                        29
                                                - gerarEstrelas(
                                                        avaliacao.getNota()).length()
                                                - String.valueOf(
                                                        avaliacao.getNota()).length())));

                System.out.printf(
                        "│ %-38s │%n",
                        limitarTexto(comentario, 38));

                System.out.println(
                        "└────────────────────────────────────────┘");
            }
        }

        if (!encontrou) {

            System.out.println();
            System.out.println(
                    "[INFO] Essa skin ainda não possui avaliações.");
        }

        System.out.println();
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    private int contarAvaliacoes(Skin skin) {

        EntidadeDAO daoAvaliacao = banco.getDAO(Avaliacao.class);

        Entidade[] avaliacoes = daoAvaliacao.carregar();

        int quantidade = 0;

        for (Entidade entidade : avaliacoes) {

            Avaliacao avaliacao = (Avaliacao) entidade;

            if (avaliacao.getSkin() != null
                    && avaliacao.getSkin().getId() == skin.getId()) {

                quantidade++;
            }
        }

        return quantidade;
    }

    private String limitarTexto(String texto, int tamanho) {

        if (texto == null) {
            return "";
        }

        if (texto.length() <= tamanho) {
            return texto;
        }

        return texto.substring(0, tamanho - 3) + "...";
    }

    private void pesquisarSkin() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("           PESQUISAR SKINS");
        System.out.println("========================================");
        System.out.println("1 - Pesquisar por nome");
        System.out.println("2 - Pesquisar por jogo");
        System.out.println("3 - Pesquisar por raridade");
        System.out.println("0 - Voltar");
        System.out.println("========================================");
        System.out.println("========================================");

        int opcao = lerInt("Escolha uma opção: ");

        if (opcao == 0) {
            return;
        }

        if (opcao < 1 || opcao > 3) {
            System.out.println("[ERRO] Opção inválida.");
            return;
        }

        System.out.println("Digite o que deseja pesquisar: ");
        String busca = scanner.nextLine();

        if (busca.isEmpty()) {
            System.out.println("[ERRO] A pesquisa não pode estar vazia.");
            return;
        }

        EntidadeDAO daoSkin = banco.getDAO(Skin.class);
        Entidade[] skins = daoSkin.carregar();

        int encontrados = 0;

        System.out.println();
        System.out.println("========================================");
        System.out.println("             RESULTADOS");
        System.out.println("========================================");

        for (Entidade entidade : skins) {
            Skin skin = (Skin) entidade;

            String valor;

            switch (opcao) {
                case 1:
                    valor = skin.getNome();
                    break;

                case 2:
                    valor = skin.getJogo();
                    break;

                case 3:
                    valor = skin.getRaridade();
                    break;

                default:
                    continue;
            }

            if (valor != null
                    && valor.toLowerCase().contains(busca.toLowerCase())) {

                encontrados++;

                System.out.println();
                System.out.println("┌──────────────────────────────────────┐");
                System.out.println("│               SKIN                   │");
                System.out.println("├──────────────────────────────────────┤");
                System.out.println("│ ID: " + skin.getId());
                System.out.println("│ Nome: " + skin.getNome());
                System.out.println("│ Jogo: " + skin.getJogo());
                System.out.println("│ Raridade: " + skin.getRaridade());
                System.out.println("└──────────────────────────────────────┘");
            }
        }

        System.out.println();

        if (encontrados == 0) {
            System.out.println("[!] Nenhuma skin encontrada.");
        } else {
            System.out.println("[OK] " + encontrados
                    + " skin(s) encontrada(s).");
        }

        System.out.println();
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }

    // =====================================================
    // CRUD - AVALIACAO
    // =====================================================

    private void inserirAvaliacao() {

        int id = lerInt("ID da avaliacao: ");

        int idUsuario = lerInt("ID do usuario: ");

        Usuario usuario = (Usuario) dao(Usuario.class)
                .buscar(idUsuario);

        int idSkin = lerInt("ID da skin: ");

        Skin skin = (Skin) dao(Skin.class)
                .buscar(idSkin);

        if (usuario == null || skin == null) {

            System.out.println(
                    "Usuario ou skin nao encontrado.");

            return;
        }

        int nota;

        do {

            nota = lerInt("Nota (1 a 10): ");

            if (nota < 1 || nota > 10) {

                System.out.println(
                        "A nota deve estar entre 1 e 10.");
            }

        } while (nota < 1 || nota > 10);

        System.out.print("Comentario: ");
        String comentario = scanner.nextLine();

        Avaliacao avaliacao = new Avaliacao(
                id,
                usuario,
                skin,
                nota,
                comentario);

        if (dao(Avaliacao.class)
                .salvar(avaliacao)) {

            System.out.println(
                    "Avaliacao salva.");

        } else {

            System.out.println(
                    "Nao foi possivel salvar.");
        }
    }

    private void alterarAvaliacao() {

        int id = lerInt("ID da avaliacao: ");

        Avaliacao avaliacao = (Avaliacao) dao(Avaliacao.class)
                .buscar(id);

        if (avaliacao == null) {

            System.out.println(
                    "Avaliacao nao encontrada.");

            return;
        }

        int nota;

        do {

            nota = lerInt("Nova nota (1 a 10): ");

        } while (nota < 1 || nota > 10);

        System.out.print("Novo comentario: ");

        String comentario = scanner.nextLine();

        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);

        dao(Avaliacao.class)
                .atualizar(avaliacao);

        System.out.println(
                "Avaliacao atualizada.");
    }

    // =====================================================
    // CRUD - COLECAO
    // =====================================================

    private void inserirColecao() {

        int id = lerInt("ID da colecao: ");

        int idUsuario = lerInt("ID do usuario dono: ");

        Usuario usuario = (Usuario) dao(Usuario.class)
                .buscar(idUsuario);

        if (usuario == null) {

            System.out.println(
                    "Usuario nao encontrado.");

            return;
        }

        System.out.print("Nome da colecao: ");

        String nome = scanner.nextLine();

        Colecao colecao = new Colecao(
                id,
                usuario,
                nome);

        gerenciarItens(colecao);

        if (dao(Colecao.class)
                .salvar(colecao)) {

            usuario.setColecao(colecao);

            dao(Usuario.class)
                    .atualizar(usuario);

            System.out.println(
                    "Colecao salva.");

        } else {

            System.out.println(
                    "Nao foi possivel salvar.");
        }
    }

    private void alterarColecao() {

        int id = lerInt("ID da colecao: ");

        Colecao colecao = (Colecao) dao(Colecao.class)
                .buscar(id);

        if (colecao == null) {

            System.out.println(
                    "Colecao nao encontrada.");

            return;
        }

        System.out.print("Novo nome: ");

        colecao.setNome(
                scanner.nextLine());

        gerenciarItens(colecao);

        dao(Colecao.class)
                .atualizar(colecao);

        System.out.println(
                "Colecao atualizada.");
    }

    // ==========================================
    // ITENS DA COLECAO
    // ==========================================

    private void gerenciarItens(
            Colecao colecao) {

        int opcao;

        do {

            System.out.println();
            System.out.println(
                    "Itens da colecao:");

            for (ItemColecao item : colecao.getItens()) {

                System.out.println(
                        " - " + item);
            }

            System.out.println();
            System.out.println(
                    "1 - Adicionar skin");
            System.out.println(
                    "2 - Remover skin");
            System.out.println(
                    "0 - Finalizar");

            System.out.print("Opcao: ");

            opcao = lerInt();

            if (opcao == 1) {

                int idSkin = lerInt("ID da skin: ");

                Skin skin = (Skin) dao(Skin.class)
                        .buscar(idSkin);

                if (skin == null) {

                    System.out.println(
                            "Skin nao encontrada.");

                    continue;
                }

                int quantidade = lerInt("Quantidade: ");

                if (quantidade <= 0) {

                    System.out.println(
                            "Quantidade invalida.");

                    continue;
                }

                colecao.adicionarItem(
                        skin,
                        quantidade);

                System.out.println(
                        "Skin adicionada.");

            } else if (opcao == 2) {

                int idSkin = lerInt(
                        "ID da skin a remover: ");

                if (colecao.removerItem(idSkin)) {

                    System.out.println(
                            "Skin removida.");

                } else {

                    System.out.println(
                            "Skin nao encontrada na colecao.");
                }
            }

        } while (opcao != 0);
    }

    // =====================================================
    // OPERACOES GENERICAS
    // =====================================================

    private void apagar(
            Class<? extends Entidade> tipo) {

        int id = lerInt("ID: ");

        Entidade removida = dao(tipo).apagar(id);

        if (removida == null) {

            System.out.println(
                    "Entidade nao encontrada.");

        } else {

            System.out.println(
                    "Entidade removida:");

            System.out.println(removida);
        }
    }

    private void visualizarPorId(
            Class<? extends Entidade> tipo) {

        int id = lerInt("ID: ");

        Entidade entidade = dao(tipo).buscar(id);

        if (entidade == null) {

            System.out.println(
                    "Nao encontrado.");

            return;
        }

        System.out.println(entidade);

        if (entidade instanceof Colecao) {

            Colecao colecao = (Colecao) entidade;

            System.out.println("Itens:");

            for (ItemColecao item : colecao.getItens()) {

                System.out.println(
                        "  " + item);
            }
        }
    }

    private void visualizarTodos(
            Class<? extends Entidade> tipo) {

        Entidade[] entidades = dao(tipo).carregar();

        if (entidades.length == 0) {

            System.out.println(
                    "Nenhum registro.");

            return;
        }

        for (Entidade entidade : entidades) {

            System.out.println(
                    entidade);

            if (entidade instanceof Colecao) {

                Colecao colecao = (Colecao) entidade;

                for (ItemColecao item : colecao.getItens()) {

                    System.out.println(
                            "  " + item);
                }
            }
        }
    }

    private EntidadeDAO dao(
            Class<? extends Entidade> tipo) {

        return banco.getDAO(tipo);
    }

    // =====================================================
    // METODOS AUXILIARES
    // =====================================================

    // ==========================================
    // LEITURA DE INTEIRO
    // ==========================================

    private int lerInt() {

        while (true) {

            try {

                return Integer.parseInt(
                        scanner.nextLine());

            } catch (NumberFormatException e) {

                System.out.print(
                        "Digite um numero valido: ");
            }
        }
    }

    private int lerInt(String mensagem) {

        System.out.print(mensagem);

        return lerInt();
    }

    private int proximoId(Class<? extends Entidade> tipo) {

        EntidadeDAO dao = banco.getDAO(tipo);

        Entidade[] entidades = dao.carregar();

        if (entidades.length == 0) {
            return 1;
        }

        int maiorId = 0;

        for (Entidade entidade : entidades) {

            if (entidade.getId() > maiorId) {
                maiorId = entidade.getId();
            }
        }

        return maiorId + 1;
    }

    private int compararTexto(String texto1, String texto2) {
        if (texto1 == null) {
            texto1 = "";
        }

        if (texto2 == null) {
            texto2 = "";
        }

        return texto1.compareToIgnoreCase(texto2);
    }

    private double calcularMedia(Skin skin) {
        EntidadeDAO daoAvaliacao = banco.getDAO(Avaliacao.class);

        Entidade[] entidades = daoAvaliacao.carregar();

        double soma = 0;
        int quantidade = 0;

        for (Entidade entidade : entidades) {
            Avaliacao avaliacao = (Avaliacao) entidade;

            if (avaliacao.getSkin() != null
                    && avaliacao.getSkin().getId() == skin.getId()) {

                soma += avaliacao.getNota();
                quantidade++;
            }
        }

        if (quantidade == 0) {
            return 0;
        }

        return soma / quantidade;
    }

    // =====================================================
    // ARQUIVOS
    // =====================================================
    private void carregarSkinsTxt() {

        String arquivo = "skins.txt";

        EntidadeDAO daoSkin = banco.getDAO(Skin.class);

        try (
                BufferedReader leitor = new BufferedReader(
                        new FileReader(arquivo))) {

            String linha;

            while ((linha = leitor.readLine()) != null) {

                // Ignora linhas vazias
                if (linha.trim().isEmpty()) {
                    continue;
                }

                // Ignora comentários do arquivo
                if (linha.startsWith("#")) {
                    continue;
                }

                String[] dados = linha.split(";", -1);

                // ID;NOME;JOGO;RARIDADE
                if (dados.length != 4) {
                    continue;
                }

                try {

                    int id = Integer.parseInt(
                            dados[0].trim());

                    String nome = dados[1].trim();
                    String jogo = dados[2].trim();
                    String raridade = dados[3].trim();

                    // Verifica se a skin já existe
                    if (daoSkin.buscar(id) != null) {
                        continue;
                    }

                    Skin skin = new Skin(
                            id,
                            nome,
                            jogo,
                            raridade,
                            null);

                    daoSkin.salvar(skin);

                } catch (NumberFormatException e) {

                    System.out.println(
                            "ID invalido no arquivo skins.txt: "
                                    + linha);
                }
            }

            // Salva as skins carregadas
            daoSkin.persistir();

            System.out.println(
                    "Skins carregadas com sucesso!");

        } catch (IOException e) {

            System.out.println(
                    "Nao foi possivel carregar o arquivo skins.txt.");

            System.out.println(
                    "Erro: " + e.getMessage());
        }
    }

}