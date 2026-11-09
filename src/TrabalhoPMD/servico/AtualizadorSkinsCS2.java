
package TrabalhoPMD.servico;

import TrabalhoPMD.dados.BancoDados;
import TrabalhoPMD.dados.EntidadeDAO;
import TrabalhoPMD.modelo.Entidade;
import TrabalhoPMD.modelo.Skin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Arrays;

public class AtualizadorSkinsCS2 {

    private final BancoDados banco;
    private final EntidadeDAO daoSkin;
    private final ServicoCS2 servico;

    public AtualizadorSkinsCS2() {

        banco = BancoDados.getInstancia();

        daoSkin = banco.getDAO(Skin.class);

        servico = new ServicoCS2();
    }

    public void atualizarImagens() {

        try {

            System.out.println("Consultando API do CS2...");

            JsonArray skinsAPI = servico.buscarSkins();

            System.out.println(
                    "Skins retornadas pela API: " + skinsAPI.size()
            );

            Entidade[] entidades = daoSkin.carregar();

            int totalCS2 = 0;
            int encontradas = 0;
            int semCorrespondencia = 0;
            int semImagem = 0;

            for (Entidade entidade : entidades) {

                Skin skin = (Skin) entidade;

                if (skin.getJogo() == null
                        || !skin.getJogo().equalsIgnoreCase("CS2")) {
                    continue;
                }

                totalCS2++;

                String nomeBanco = normalizar(skin.getNome());

                JsonObject correspondencia = buscarPorNome(
                        skinsAPI,
                        nomeBanco
                );

                if (correspondencia != null) {

                    if (correspondencia.has("image")
                            && !correspondencia.get("image").isJsonNull()
                            && !correspondencia.get("image")
                                    .getAsString().isBlank()) {

                        String imagem = correspondencia
                                .get("image")
                                .getAsString();

                        skin.setImagemUrl(imagem);

                        System.out.println(
                                "[ENCONTRADA] "
                                + skin.getNome()
                                + " -> " + imagem
                        );

                        encontradas++;

                    } else {

                        System.out.println(
                                "[SEM IMAGEM] " + skin.getNome()
                        );

                        semImagem++;
                    }

                } else {

                    System.out.println(
                            "[NAO ENCONTRADA] " + skin.getNome()
                    );

                    semCorrespondencia++;
                }
            }

            if (daoSkin.persistir()) {

                System.out.println(
                        "\nImagens salvas com sucesso no banco de dados!"
                );

            } else {

                System.out.println(
                        "\nErro ao salvar as imagens."
                );
            }

            System.out.println("\n===== RELATORIO =====");

            System.out.println(
                    "Skins do CS2 no banco: " + totalCS2
            );

            System.out.println(
                    "Correspondencias encontradas: " + encontradas
            );

            System.out.println(
                    "Sem imagem: " + semImagem
            );

            System.out.println(
                    "Sem correspondencia: " + semCorrespondencia
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao atualizar imagens do CS2."
            );

            e.printStackTrace();
        }
    }

    private JsonObject buscarPorNome(
            JsonArray lista,
            String nomeNormalizado) {

        for (int i = 0; i < lista.size(); i++) {

            JsonObject skinAPI =
                    lista.get(i).getAsJsonObject();

            if (!skinAPI.has("name")
                    || skinAPI.get("name").isJsonNull()) {
                continue;
            }

            String nomeAPI = normalizar(
                    skinAPI.get("name").getAsString()
            );

            if (nomeNormalizado.equals(nomeAPI)) {
                return skinAPI;
            }
        }

        return null;
    }

    private String normalizar(String texto) {

        if (texto == null) {
            return "";
        }

        String normalizado = Normalizer.normalize(
                texto,
                Normalizer.Form.NFD
        )
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", " ")
                .trim();

        if (normalizado.isEmpty()) {
            return "";
        }

        String[] palavras = normalizado.split("\\s+");

        Arrays.sort(palavras);

        return String.join(" ", palavras);
    }
}
