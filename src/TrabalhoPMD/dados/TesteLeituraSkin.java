package TrabalhoPMD.dados;

import TrabalhoPMD.modelo.Entidade;
import TrabalhoPMD.modelo.Skin;

public class TesteLeituraSkin {

    public static void main(String[] args) {

        BancoDados banco = BancoDados.getInstancia();

        banco.recuperarTodos();

        EntidadeDAO dao = banco.getDAO(Skin.class);

        Entidade[] entidades = dao.carregar();

        int comImagem = 0;
        int semImagem = 0;

        for (Entidade entidade : entidades) {

            Skin skin = (Skin) entidade;

            if ("Valorant".equalsIgnoreCase(skin.getJogo())) {

                if (skin.getImagemUrl() != null
                        && !skin.getImagemUrl().isBlank()) {

                    comImagem++;

                    System.out.println(
                            skin.getNome() + " | "
                            + skin.getImagemUrl()
                    );

                } else {
                    semImagem++;
                }
            }
        }

        System.out.println("\n===== RESULTADO =====");
        System.out.println("Total com imagem: " + comImagem);
        System.out.println("Total sem imagem: " + semImagem);
        System.out.println("Total de skins: " + entidades.length);
    }
}