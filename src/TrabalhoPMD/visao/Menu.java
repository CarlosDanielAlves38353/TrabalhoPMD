package TrabalhoPMD.visao;

import TrabalhoPMD.dados.BancoDados;
import TrabalhoPMD.dados.EntidadeDAO;
import TrabalhoPMD.modelo.Avaliacao;
import TrabalhoPMD.modelo.Colecao;
import TrabalhoPMD.modelo.Entidade;
import TrabalhoPMD.modelo.ItemColecao;
import TrabalhoPMD.modelo.Skin;
import TrabalhoPMD.modelo.Usuario;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Menu {

    private Scanner scanner;
    private BancoDados banco;
    private Usuario usuarioLogado;

    public Menu() {
        scanner = new Scanner(System.in);
        banco = BancoDados.getInstancia();
    }

   public void executar() {

    banco.recuperarTodos();
    carregarSkinsTxt();

    menuInicial();

    banco.persistirTodos();

    scanner.close();
}

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
                    if (usuarioLogado.getPerfil()
                            == TrabalhoPMD.modelo.Perfil.ADMIN) {

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
            TrabalhoPMD.modelo.Perfil.USUARIO
    );

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

private void menuPrincipalAdmin() {

    int opcao;

    do {

        System.out.println();
        System.out.println("========================================");
        System.out.println("          PAINEL ADMINISTRATIVO");
        System.out.println("========================================");
        System.out.println("Administrador: " + usuarioLogado.getNome());
        System.out.println("========================================");
        System.out.println("1 - Gerenciar usuarios");
        System.out.println("2 - Gerenciar skins");
        System.out.println("3 - Gerenciar avaliacoes");
        System.out.println("4 - Gerenciar colecoes");
        System.out.println("5 - Visualizar skins por jogo");
        System.out.println("0 - Logout");
        System.out.println("========================================");
        System.out.print("Opcao: ");

        opcao = lerInt();

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

            case 0:
                usuarioLogado = null;
                System.out.println("Logout realizado.");
                break;

            default:
                System.out.println("Opcao invalida.");
        }

    } while (opcao != 0);
}

    //
    // MENU LOGIN
    //

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

//
// MENU INICIAR
//

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
        System.out.println("║ 2 - Minha coleção                    ║");
        System.out.println("║ 3 - Meu perfil                       ║");
        System.out.println("║ 4 - Logout                           ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("Escolha: ");

        int opcao = lerInt();

        switch (opcao) {

            case 1:
                visualizarSkinsPorJogo();
                break;

            case 2:
                System.out.println("Minha coleção - em desenvolvimento.");
                break;

            case 3:
                System.out.println(usuarioLogado);
                break;

            case 4:
                usuarioLogado = null;
                System.out.println("Logout realizado.");
                return;

            default:
                System.out.println("Opcao invalida.");
        }
    }
}

    // ==========================================
    // MENU DE USUARIO
    // ==========================================



    
    private void menuUsuario() {

        int opcao;

        do {

            System.out.println();
            System.out.println("===== MENU USUARIOS =====");
            System.out.println("1 - Inserir");
            System.out.println("2 - Alterar");
            System.out.println("3 - Apagar");
            System.out.println("4 - Visualizar por ID");
            System.out.println("5 - Visualizar todos");
            System.out.println("0 - Voltar");
            System.out.print("Opcao: ");

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
    // MENU DE SKIN
    // ==========================================

    private void menuSkin() {

        int opcao;

        do {

            System.out.println();
            System.out.println("===== MENU SKIN =====");
            System.out.println("1 - Inserir");
            System.out.println("2 - Alterar");
            System.out.println("3 - Apagar");
            System.out.println("4 - Visualizar por ID");
            System.out.println("5 - Visualizar todos");
            System.out.println("6 - Visualizar por jogo");
            System.out.println("0 - Voltar");
            System.out.print("Opcao: ");

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
                     visualizarSkinsPorJogo();
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
            System.out.println("===== MENU AVALIACOES =====");
            System.out.println("1 - Inserir");
            System.out.println("2 - Alterar");
            System.out.println("3 - Apagar");
            System.out.println("4 - Visualizar por ID");
            System.out.println("5 - Visualizar todos");
            System.out.println("0 - Voltar");
            System.out.print("Opcao: ");

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
            System.out.println("===== COLECOES =====");
            System.out.println("1 - Inserir");
            System.out.println("2 - Alterar");
            System.out.println("3 - Apagar");
            System.out.println("4 - Visualizar por ID");
            System.out.println("5 - Visualizar todos");
            System.out.println("0 - Voltar");
            System.out.print("Opcao: ");

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

    // ==========================================
    // USUARIO
    // ==========================================

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

        Usuario usuario =
                new Usuario(id, nome, email, senha);

        if (dao(Usuario.class).salvar(usuario)) {

            System.out.println("Usuario salvo.");

        } else {

            System.out.println(
                    "Nao foi possivel salvar o usuario."
            );
        }
    }

    private void alterarUsuario() {

        int id = lerInt("ID do usuario: ");

        Usuario usuario =
                (Usuario) dao(Usuario.class).buscar(id);

        if (usuario == null) {

            System.out.println(
                    "Usuario nao encontrado."
            );

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
                "Usuario atualizado."
        );
    }

    // ==========================================
    // SKIN
    // ==========================================

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

        Entidade entidadeUsuario =
                daoUsuario.buscar(idUsuario);

        if (entidadeUsuario == null) {

            System.out.println(
                    "Usuario nao encontrado."
            );

            return;
        }

        usuario = (Usuario) entidadeUsuario;

    } else if (opcao != 2) {

        System.out.println(
                "Opcao invalida."
        );

        return;
    }

    Skin skin = new Skin(
            id,
            nome,
            jogo,
            raridade,
            usuario
    );

    if (daoSkin.salvar(skin)) {

        daoSkin.persistir();

        System.out.println();
        System.out.println(
                "Skin cadastrada com sucesso!"
        );

    } else {

        System.out.println();
        System.out.println(
                "Erro ao cadastrar a skin."
        );
    }
}

    private void alterarSkin() {

        int id = lerInt("ID da skin: ");

        Skin skin =
                (Skin) dao(Skin.class).buscar(id);

        if (skin == null) {

            System.out.println(
                    "Skin nao encontrada."
            );

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
                "Skin atualizada."
        );
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

    while (true) {

        System.out.println();
        System.out.println("===== FILTRAR SKINS POR JOGO =====");

        for (int i = 0; i < jogos.size(); i++) {
            System.out.println((i + 1) + " - " + jogos.get(i));
        }

        System.out.println("0 - Voltar");
        System.out.print("Escolha: ");

        int opcao = lerInt();

        if (opcao == 0) {
            return;
        }

        if (opcao < 1 || opcao > jogos.size()) {
            System.out.println();
            System.out.println("Opcao invalida.");
            continue;
        }

        String jogoSelecionado = jogos.get(opcao - 1);

        System.out.println();
        System.out.println(
                "===== SKINS DE " +
                jogoSelecionado.toUpperCase() +
                " ====="
        );

        boolean encontrou = false;

        for (Entidade entidade : entidades) {

            Skin skin = (Skin) entidade;

            if (skin.getJogo().equalsIgnoreCase(jogoSelecionado)) {

                System.out.println(skin);
                encontrou = true;
            }
        }

        if (!encontrou) {
            System.out.println(
                    "Nenhuma skin encontrada para esse jogo."
            );
        }

        System.out.println();
        System.out.println("Pressione ENTER para continuar...");
        scanner.nextLine();
    }
}

    // ==========================================
    // AVALIACAO
    // ==========================================

    private void inserirAvaliacao() {

        int id = lerInt("ID da avaliacao: ");

        int idUsuario =
                lerInt("ID do usuario: ");

        Usuario usuario =
                (Usuario) dao(Usuario.class)
                        .buscar(idUsuario);

        int idSkin =
                lerInt("ID da skin: ");

        Skin skin =
                (Skin) dao(Skin.class)
                        .buscar(idSkin);

        if (usuario == null || skin == null) {

            System.out.println(
                    "Usuario ou skin nao encontrado."
            );

            return;
        }

        int nota;

        do {

            nota = lerInt("Nota (1 a 10): ");

            if (nota < 1 || nota > 10) {

                System.out.println(
                        "A nota deve estar entre 1 e 10."
                );
            }

        } while (nota < 1 || nota > 10);

        System.out.print("Comentario: ");
        String comentario =
                scanner.nextLine();

        Avaliacao avaliacao =
                new Avaliacao(
                        id,
                        usuario,
                        skin,
                        nota,
                        comentario
                );

        if (dao(Avaliacao.class)
                .salvar(avaliacao)) {

            System.out.println(
                    "Avaliacao salva."
            );

        } else {

            System.out.println(
                    "Nao foi possivel salvar."
            );
        }
    }

    private void alterarAvaliacao() {

        int id =
                lerInt("ID da avaliacao: ");

        Avaliacao avaliacao =
                (Avaliacao) dao(Avaliacao.class)
                        .buscar(id);

        if (avaliacao == null) {

            System.out.println(
                    "Avaliacao nao encontrada."
            );

            return;
        }

        int nota;

        do {

            nota =
                    lerInt("Nova nota (1 a 10): ");

        } while (nota < 1 || nota > 10);

        System.out.print("Novo comentario: ");

        String comentario =
                scanner.nextLine();

        avaliacao.setNota(nota);
        avaliacao.setComentario(comentario);

        dao(Avaliacao.class)
                .atualizar(avaliacao);

        System.out.println(
                "Avaliacao atualizada."
        );
    }

    // ==========================================
    // COLECAO
    // ==========================================

    private void inserirColecao() {

        int id =
                lerInt("ID da colecao: ");

        int idUsuario =
                lerInt("ID do usuario dono: ");

        Usuario usuario =
                (Usuario) dao(Usuario.class)
                        .buscar(idUsuario);

        if (usuario == null) {

            System.out.println(
                    "Usuario nao encontrado."
            );

            return;
        }

        System.out.print("Nome da colecao: ");

        String nome =
                scanner.nextLine();

        Colecao colecao =
                new Colecao(
                        id,
                        usuario,
                        nome
                );

        gerenciarItens(colecao);

        if (dao(Colecao.class)
                .salvar(colecao)) {

            usuario.setColecao(colecao);

            dao(Usuario.class)
                    .atualizar(usuario);

            System.out.println(
                    "Colecao salva."
            );

        } else {

            System.out.println(
                    "Nao foi possivel salvar."
            );
        }
    }

    private void alterarColecao() {

        int id =
                lerInt("ID da colecao: ");

        Colecao colecao =
                (Colecao) dao(Colecao.class)
                        .buscar(id);

        if (colecao == null) {

            System.out.println(
                    "Colecao nao encontrada."
            );

            return;
        }

        System.out.print("Novo nome: ");

        colecao.setNome(
                scanner.nextLine()
        );

        gerenciarItens(colecao);

        dao(Colecao.class)
                .atualizar(colecao);

        System.out.println(
                "Colecao atualizada."
        );
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
                    "Itens da colecao:"
            );

            for (ItemColecao item :
                    colecao.getItens()) {

                System.out.println(
                        " - " + item
                );
            }

            System.out.println();
            System.out.println(
                    "1 - Adicionar skin"
            );
            System.out.println(
                    "2 - Remover skin"
            );
            System.out.println(
                    "0 - Finalizar"
            );

            System.out.print("Opcao: ");

            opcao = lerInt();

            if (opcao == 1) {

                int idSkin =
                        lerInt("ID da skin: ");

                Skin skin =
                        (Skin) dao(Skin.class)
                                .buscar(idSkin);

                if (skin == null) {

                    System.out.println(
                            "Skin nao encontrada."
                    );

                    continue;
                }

                int quantidade =
                        lerInt("Quantidade: ");

                if (quantidade <= 0) {

                    System.out.println(
                            "Quantidade invalida."
                    );

                    continue;
                }

                colecao.adicionarItem(
                        skin,
                        quantidade
                );

                System.out.println(
                        "Skin adicionada."
                );

            } else if (opcao == 2) {

                int idSkin =
                        lerInt(
                                "ID da skin a remover: "
                        );

                if (colecao.removerItem(idSkin)) {

                    System.out.println(
                            "Skin removida."
                    );

                } else {

                    System.out.println(
                            "Skin nao encontrada na colecao."
                    );
                }
            }

        } while (opcao != 0);
    }

    // ==========================================
    // OPERACOES GENERICAS
    // ==========================================

    private void apagar(
            Class<? extends Entidade> tipo) {

        int id = lerInt("ID: ");

        Entidade removida =
                dao(tipo).apagar(id);

        if (removida == null) {

            System.out.println(
                    "Entidade nao encontrada."
            );

        } else {

            System.out.println(
                    "Entidade removida:"
            );

            System.out.println(removida);
        }
    }

    private void visualizarPorId(
            Class<? extends Entidade> tipo) {

        int id = lerInt("ID: ");

        Entidade entidade =
                dao(tipo).buscar(id);

        if (entidade == null) {

            System.out.println(
                    "Nao encontrado."
            );

            return;
        }

        System.out.println(entidade);

        if (entidade instanceof Colecao) {

            Colecao colecao =
                    (Colecao) entidade;

            System.out.println("Itens:");

            for (ItemColecao item :
                    colecao.getItens()) {

                System.out.println(
                        "  " + item
                );
            }
        }
    }

    private void visualizarTodos(
            Class<? extends Entidade> tipo) {

        Entidade[] entidades =
                dao(tipo).carregar();

        if (entidades.length == 0) {

            System.out.println(
                    "Nenhum registro."
            );

            return;
        }

        for (Entidade entidade :
                entidades) {

            System.out.println(
                    entidade
            );

            if (entidade instanceof Colecao) {

                Colecao colecao =
                        (Colecao) entidade;

                for (ItemColecao item :
                        colecao.getItens()) {

                    System.out.println(
                            "  " + item
                    );
                }
            }
        }
    }

    // ==========================================
    // DAO
    // ==========================================

    private EntidadeDAO dao(
            Class<? extends Entidade> tipo) {

        return banco.getDAO(tipo);
    }

    // ==========================================
    // LEITURA DE INTEIRO
    // ==========================================

    private int lerInt() {

        while (true) {

            try {

                return Integer.parseInt(
                        scanner.nextLine()
                );

            } catch (NumberFormatException e) {

                System.out.print(
                        "Digite um numero valido: "
                );
            }
        }
    }

    private int lerInt(String mensagem) {

        System.out.print(mensagem);

        return lerInt();
    }
    private void carregarSkinsTxt() {

    String arquivo = "skins.txt";

    EntidadeDAO daoSkin = banco.getDAO(Skin.class);

    try (
        BufferedReader leitor =
                new BufferedReader(
                        new FileReader(arquivo)
                )
    ) {

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
                        dados[0].trim()
                );

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
                        null
                );

                daoSkin.salvar(skin);

            } catch (NumberFormatException e) {

                System.out.println(
                        "ID invalido no arquivo skins.txt: "
                        + linha
                );
            }
        }

        // Salva as skins carregadas
        daoSkin.persistir();

        System.out.println(
                "Skins carregadas com sucesso!"
        );

    } catch (IOException e) {

        System.out.println(
                "Nao foi possivel carregar o arquivo skins.txt."
        );

        System.out.println(
                "Erro: " + e.getMessage()
        );
    }
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
}