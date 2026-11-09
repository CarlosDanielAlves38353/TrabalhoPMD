package TrabalhoPMD.servico;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ServicoLeagueOfLegends {

    private static final String URL_BASE =
            "https://ddragon.leagueoflegends.com";

    private final HttpClient cliente;

    public ServicoLeagueOfLegends() {

        cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    // Busca as skins dos campeões relacionados aos nomes cadastrados
    public JsonArray buscarSkins(List<String> nomesCadastrados)
            throws IOException, InterruptedException {

        System.out.println("Consultando Data Dragon...");

        String versao = buscarVersao();

        System.out.println("Versão: " + versao);

        // Busca a lista de campeões nos dois idiomas
        JsonObject campeoesPt = buscarListaCampeoes(
                versao,
                "pt_BR"
        );

        JsonObject campeoesEn = buscarListaCampeoes(
                versao,
                "en_US"
        );

        // Descobre quais campeões aparecem nas skins do banco
        Set<String> campeoes = new LinkedHashSet<>();

        localizarCampeoes(
                campeoes,
                campeoesPt,
                nomesCadastrados
        );

        localizarCampeoes(
                campeoes,
                campeoesEn,
                nomesCadastrados
        );

        System.out.println(
                "Campeões identificados: " + campeoes.size()
        );

        if (campeoes.isEmpty()) {
            throw new IOException(
                    "Não foi possível identificar os campeões cadastrados."
            );
        }

        JsonArray resultado = new JsonArray();

        // Consulta os dados de cada campeão nos dois idiomas
        for (String campeaoId : campeoes) {

            System.out.println(
                    "Consultando campeão: " + campeaoId
            );

            buscarSkinsCampeao(
                    resultado,
                    versao,
                    campeaoId,
                    "pt_BR"
            );

            buscarSkinsCampeao(
                    resultado,
                    versao,
                    campeaoId,
                    "en_US"
            );
        }

        System.out.println(
                "Total de skins recebidas: " + resultado.size()
        );

        return resultado;
    }

    // Busca a versão mais recente do Data Dragon
    private String buscarVersao()
            throws IOException, InterruptedException {

        String url = URL_BASE + "/api/versions.json";

        HttpResponse<String> resposta = requisitar(url);

        JsonArray versoes = JsonParser
                .parseString(resposta.body())
                .getAsJsonArray();

        if (versoes.isEmpty()) {
            throw new IOException(
                    "Não foi possível encontrar a versão do Data Dragon."
            );
        }

        return versoes.get(0).getAsString();
    }

    // Busca a lista resumida de campeões
    private JsonObject buscarListaCampeoes(
            String versao,
            String idioma)
            throws IOException, InterruptedException {

        String url = URL_BASE
                + "/cdn/" + versao
                + "/data/" + idioma
                + "/champion.json";

        HttpResponse<String> resposta = requisitar(url);

        JsonObject json = JsonParser
                .parseString(resposta.body())
                .getAsJsonObject();

        return json.getAsJsonObject("data");
    }

    // Identifica os campeões presentes nos nomes das skins
    private void localizarCampeoes(
            Set<String> campeoes,
            JsonObject listaCampeoes,
            List<String> nomesCadastrados) {

        for (Map.Entry<String, JsonElement> entrada
                : listaCampeoes.entrySet()) {

            JsonObject campeao = entrada.getValue()
                    .getAsJsonObject();

            String nomeCampeao = campeao
                    .get("name")
                    .getAsString();

            String idCampeao = campeao
                    .get("id")
                    .getAsString();

            String nomeNormalizado = normalizarTexto(nomeCampeao);

            for (String nomeSkin : nomesCadastrados) {

                String skinNormalizada = normalizarTexto(nomeSkin);

                String buscaSkin = " " + skinNormalizada + " ";
                String buscaCampeao = " " + nomeNormalizado + " ";

                if (buscaSkin.contains(buscaCampeao)) {

                    campeoes.add(idCampeao);

                    break;
                }
            }
        }
    }

    // Busca os dados completos de um campeão
    private JsonObject buscarDadosCampeao(
            String versao,
            String campeaoId,
            String idioma)
            throws IOException, InterruptedException {

        String url = URL_BASE
                + "/cdn/" + versao
                + "/data/" + idioma
                + "/champion/" + campeaoId + ".json";

        HttpResponse<String> resposta = requisitar(url);

        JsonObject json = JsonParser
                .parseString(resposta.body())
                .getAsJsonObject();

        JsonObject dados = json.getAsJsonObject("data");

        return dados.getAsJsonObject(campeaoId);
    }

    // Adiciona as skins de um campeão ao resultado
    private void buscarSkinsCampeao(
            JsonArray resultado,
            String versao,
            String campeaoId,
            String idioma)
            throws IOException, InterruptedException {

        JsonObject campeao = buscarDadosCampeao(
                versao,
                campeaoId,
                idioma
        );

        JsonArray skins = campeao.getAsJsonArray("skins");

        for (int i = 0; i < skins.size(); i++) {

            JsonObject skin = skins.get(i).getAsJsonObject();

            if (!skin.has("name")
                    || !skin.has("num")) {
                continue;
            }

            // Ignora skins cromáticas
            if (skin.has("parentSkin")
                    && !skin.get("parentSkin").isJsonNull()) {
                continue;
            }

            String nome = skin.get("name").getAsString();

            // Ignora a skin padrão do campeão
            if (nome.equalsIgnoreCase("default")) {
                continue;
            }

            int numero = skin.get("num").getAsInt();

            String imagem = construirUrlImagem(
                    campeaoId,
                    numero
            );

            JsonObject resultadoSkin = new JsonObject();

            resultadoSkin.addProperty("displayName", nome);
            resultadoSkin.addProperty("displayIcon", imagem);

            resultado.add(resultadoSkin);
        }
    }

    // Monta a URL da splash art
    private String construirUrlImagem(
            String campeaoId,
            int numero) {

        // Exceção no nome do arquivo de Fiddlesticks
        if (campeaoId.equalsIgnoreCase("Fiddlesticks")) {
            campeaoId = "FiddleSticks";
        }

        return URL_BASE
                + "/cdn/img/champion/splash/"
                + campeaoId + "_" + numero + ".jpg";
    }

    // Realiza uma requisição HTTP
    private HttpResponse<String> requisitar(String url)
            throws IOException, InterruptedException {

        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<String> resposta = cliente.send(
                requisicao,
                HttpResponse.BodyHandlers.ofString()
        );

        if (resposta.statusCode() != 200) {

            throw new IOException(
                    "Erro na API do League of Legends: "
                    + resposta.statusCode()
                    + " | URL: " + url
            );
        }

        return resposta;
    }

    // Normaliza nomes para identificar os campeões
    private String normalizarTexto(String texto) {

        if (texto == null) {
            return "";
        }

        return Normalizer.normalize(
                texto,
                Normalizer.Form.NFD
        )
                .replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", " ")
                .trim();
    }
}