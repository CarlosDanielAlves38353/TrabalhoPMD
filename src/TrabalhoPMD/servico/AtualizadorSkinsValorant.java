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

public class AtualizadorSkinsValorant {

    private final BancoDados banco;
    private final EntidadeDAO daoSkin;
    private final ServicoValorant servico;

    public AtualizadorSkinsValorant() {

        banco = BancoDados.getInstancia();

        daoSkin = banco.getDAO(Skin.class);

        servico = new ServicoValorant();
    }

    public void atualizarImagens() {

        try {

            System.out.println("Consultando API do Valorant...");

            JsonArray skinsAPIpt = servico.buscarSkins("pt-BR");

            JsonArray skinsAPIen = servico.buscarSkins("en-US");

            System.out.println(
                    "Skins em português: " + skinsAPIpt.size()
            );

            System.out.println(
                    "Skins em inglês: " + skinsAPIen.size()
            );

            Entidade[] entidades = daoSkin.carregar();

            int totalValorant = 0;
            int encontradas = 0;
            int semCorrespondencia = 0;
            int semImagem = 0;

            for (Entidade entidade : entidades) {

                Skin skin = (Skin) entidade;

                if (skin.getJogo() == null
                        || !skin.getJogo().equalsIgnoreCase("Valorant")) {
                    continue;
                }

                totalValorant++;

                String nomeBanco = normalizar(skin.getNome());

                boolean encontrou = false;

                JsonObject correspondencia = buscarPorNome(
                        skinsAPIpt,
                        nomeBanco
                );

                if (correspondencia == null) {

                    correspondencia = buscarPorNome(
                            skinsAPIen,
                            nomeBanco
                    );
                }

                if (correspondencia != null) {

                    encontrou = true;

                    if (correspondencia.has("displayIcon")
                            && !correspondencia.get("displayIcon").isJsonNull()
                            && !correspondencia.get("displayIcon")
                                    .getAsString().isBlank()) {

                        String imagem = correspondencia
                                .get("displayIcon")
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
            "Imagens salvas com sucesso no banco de dados!"
    );

} else {

    System.out.println(
            "Erro ao salvar as imagens."
    );
}
            
            System.out.println("\n===== RELATORIO =====");

            System.out.println(
                    "Skins do Valorant no banco: " + totalValorant
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
                    "Erro ao atualizar imagens do Valorant."
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

        if (!skinAPI.has("displayName")
                || skinAPI.get("displayName").isJsonNull()) {
            continue;
        }

        String nomeAPI = normalizar(
                skinAPI.get("displayName").getAsString()
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
