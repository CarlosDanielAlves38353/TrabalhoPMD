package TrabalhoPMD.dados;

import TrabalhoPMD.modelo.Colecao;
import TrabalhoPMD.modelo.Skin;
import TrabalhoPMD.modelo.Usuario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorDados {

    private BancoDados banco;

    public LeitorDados() {
        banco = BancoDados.getInstancia();
    }

    /**
     * Carrega coleções de um arquivo TXT.
     *
     * Formato:
     *
     * ID_COLECAO;ID_USUARIO;NOME;ID_SKIN:QUANTIDADE,ID_SKIN:QUANTIDADE
     *
     * Exemplo:
     *
     * 1;1;Minhas Skins;1:1,3:2,18:1
     */
    public void carregarColecoesTxt(String arquivo) {

        EntidadeDAO daoColecao = banco.getDAO(Colecao.class);
        EntidadeDAO daoUsuario = banco.getDAO(Usuario.class);
        EntidadeDAO daoSkin = banco.getDAO(Skin.class);

        try (BufferedReader leitor =
                     new BufferedReader(new FileReader(arquivo))) {

            String linha;

            while ((linha = leitor.readLine()) != null) {

                linha = linha.trim();

                // Ignora linhas vazias e comentários
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                String[] partes = linha.split(";");

                if (partes.length != 4) {
                    System.out.println(
                            "[AVISO] Linha inválida no arquivo: " + linha);
                    continue;
                }

                try {

                    int idColecao = Integer.parseInt(partes[0]);
                    int idUsuario = Integer.parseInt(partes[1]);

                    String nome = partes[2];

                    Usuario usuario =
                            (Usuario) daoUsuario.buscar(idUsuario);

                    if (usuario == null) {
                        System.out.println(
                                "[AVISO] Usuário " + idUsuario
                                + " não encontrado.");
                        continue;
                    }

                    // Evita duplicar coleções
                    if (daoColecao.buscar(idColecao) != null) {
                        continue;
                    }

                    Colecao colecao =
                            new Colecao(idColecao, usuario, nome);

                    String[] itens = partes[3].split(",");

                    for (String itemTexto : itens) {

                        String[] item =
                                itemTexto.trim().split(":");

                        if (item.length != 2) {
                            continue;
                        }

                        int idSkin =
                                Integer.parseInt(item[0]);

                        int quantidade =
                                Integer.parseInt(item[1]);

                        Skin skin =
                                (Skin) daoSkin.buscar(idSkin);

                        if (skin != null && quantidade > 0) {
                            colecao.adicionarItem(
                                    skin,
                                    quantidade
                            );
                        }
                    }

                    if (daoColecao.salvar(colecao)) {

                        usuario.setColecao(colecao);

                    }

                } catch (NumberFormatException e) {

                    System.out.println(
                            "[AVISO] Valores inválidos na linha: "
                            + linha);
                }
            }

            System.out.println(
                    "[OK] Coleções carregadas de " + arquivo);

        } catch (IOException e) {

            System.out.println(
                    "[ERRO] Não foi possível ler " + arquivo
                    + ": " + e.getMessage());
        }
    }

    /**
     * Carrega coleções de uma planilha CSV.
     *
     * Formato:
     *
     * ID_COLECAO;ID_USUARIO;NOME;ID_SKIN:QUANTIDADE,...
     *
     * A planilha deve ser salva como CSV.
     */
    public void carregarColecoesPlanilha(String arquivo) {
        carregarColecoesTxt(arquivo);
    }
}