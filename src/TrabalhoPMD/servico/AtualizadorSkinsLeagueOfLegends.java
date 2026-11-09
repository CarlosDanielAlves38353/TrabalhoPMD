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
import java.util.ArrayList;
import java.util.List;

public class AtualizadorSkinsLeagueOfLegends {

    private final BancoDados banco;
    private final EntidadeDAO daoSkin;
    private final ServicoLeagueOfLegends servico;

    public AtualizadorSkinsLeagueOfLegends() {

        banco = BancoDados.getInstancia();

        daoSkin = banco.getDAO(Skin.class);

        servico = new ServicoLeagueOfLegends();
    }

    public void atualizarImagens() {

        try {

            System.out.println(
                    "Consultando skins do League of Legends..."
            );

            Entidade[] entidades = daoSkin.carregar();

            List<String> nomesCadastrados = new ArrayList<>();

            int totalLeague = 0;

            // Separa as skins do League of Legends
            for (Entidade entidade : entidades) {

                Skin skin = (Skin) entidade;

                if (skin.getJogo() != null
                        && skin.getJogo().equalsIgnoreCase(
                                "League of Legends")) {

                    totalLeague++;

                    nomesCadastrados.add(skin.getNome());
                }
            }

            if (nomesCadastrados.isEmpty()) {

                System.out.println(
                        "Não existem skins de League of Legends no banco."
                );

                return;
            }

            // Consulta a API
            JsonArray skinsAPI = servico.buscarSkins(
                    nomesCadastrados
            );

            int encontradas = 0;
            int semCorrespondencia = 0;
            int semImagem = 0;

            // Compara as skins cadastradas com as da API
            for (Entidade entidade : entidades) {

                Skin skin = (Skin) entidade;

                if (skin.getJogo() == null
                        || !skin.getJogo().equalsIgnoreCase(
                                "League of Legends")) {
                    continue;
                }

                String nomeBanco = normalizar(skin.getNome());

                JsonObject correspondencia = buscarPorNome(
                        skinsAPI,
                        nomeBanco
                );

                if (correspondencia != null) {

                    if (correspondencia.has("displayIcon")
                            && !correspondencia.get("displayIcon").isJsonNull()
                            && !correspondencia.get("displayIcon")
                                    .getAsString().isBlank()) {

                        String imagem = correspondencia
                                .get("displayIcon")
                                .getAsString();

                        skin.setImagemUrl(imagem);

                        encontradas++;

                        System.out.println(
                                "[ENCONTRADA] "
                                + skin.getNome()
                        );

                    } else {

                        semImagem++;

                        System.out.println(
                                "[SEM IMAGEM] " + skin.getNome()
                        );
                    }

                } else {

                    semCorrespondencia++;

                    System.out.println(
                            "[NAO ENCONTRADA] " + skin.getNome()
                    );
                }
            }

            // Salva as alterações
            if (daoSkin.persistir()) {

                System.out.println(
                        "\nImagens salvas com sucesso!"
                );

            } else {

                System.out.println(
                        "\nErro ao salvar as imagens."
                );
            }

            // Relatório final
            System.out.println("\n===== RELATORIO =====");

            System.out.println(
                    "Skins do League of Legends no banco: "
                    + totalLeague
            );

            System.out.println(
                    "Correspondencias encontradas: "
                    + encontradas
            );

            System.out.println(
                    "Sem imagem: " + semImagem
            );

            System.out.println(
                    "Sem correspondencia: "
                    + semCorrespondencia
            );

        } catch (Exception e) {

            System.out.println(
                    "Erro ao atualizar imagens do League of Legends."
            );

            e.printStackTrace();
        }
    }

    // Procura uma skin pelo nome
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

    // Normaliza os nomes para comparação
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