package TrabalhoPMD.servico;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.time.Duration;

import java.util.HashMap;
import java.util.HashSet;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ServicoLeagueOfLegends {

    private static final String URL_BASE
            = "https://ddragon.leagueoflegends.com";
    private static final String URL_CDRAGON
            = "https://raw.communitydragon.org/latest/plugins/rcp-be-lol-game-data/global/default/v1/champions/";

    private static final Path ARQUIVO_SKINS
            = Path.of("dados", "skins_lol.json");

    private static final Gson GSON
            = new GsonBuilder().setPrettyPrinting().create();

    private final HttpClient cliente;

    public ServicoLeagueOfLegends() {

        cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    private Map<String, String> buscarRaridadesCampeao(
            String campeaoKey)
            throws IOException, InterruptedException {

        String url = URL_CDRAGON + campeaoKey + ".json";

        HttpResponse<String> resposta = requisitar(url);

        JsonObject json = JsonParser
                .parseString(resposta.body())
                .getAsJsonObject();

        JsonArray skins = json.getAsJsonArray("skins");

        Map<String, String> raridades = new HashMap<>();

        for (int i = 0; i < skins.size(); i++) {

            JsonObject skin = skins.get(i).getAsJsonObject();

            String apiId = obterTexto(skin, "id");
            String raridade = obterTexto(skin, "rarity");

            if (!apiId.isEmpty()) {

                raridades.put(
                        apiId,
                        converterRaridade(raridade)
                );
            }
        }

        return raridades;
    }

    private String converterRaridade(String raridade) {

        if (raridade == null) {
            return "Sem categoria";
        }

        switch (raridade) {

            case "kEpic":
                return "Epic";

            case "kLegendary":
                return "Legendary";

            case "kUltimate":
                return "Ultimate";

            case "kMythic":
                return "Mythic";

            case "kExalted":
                return "Exalted";

            case "kTranscendent":
                return "Transcendent";

            case "kRare":
                return "Rare";

            default:
                return "Sem categoria";
        }
    }

    // =====================================================
    // BUSCAR TODAS AS SKINS DE TODOS OS CAMPEÕES
    // =====================================================
    public JsonArray buscarSkins()
            throws IOException, InterruptedException {

        System.out.println("Consultando Data Dragon...");

        String versao = buscarVersao();

        System.out.println("Versão: " + versao);

        // Busca a lista completa de campeões
        JsonObject listaCampeoes = buscarListaCampeoes(
                versao,
                "pt_BR"
        );

        if (listaCampeoes.size() == 0) {

            throw new IOException(
                    "Nenhum campeão encontrado no Data Dragon."
            );
        }

        System.out.println(
                "Total de campeões: " + listaCampeoes.size()
        );

        // Guarda as skins pelo ID original da API
        Map<String, JsonObject> skinsPorApiId
                = new LinkedHashMap<>();

        int contador = 0;

        // Percorre todos os campeões
        for (Map.Entry<String, JsonElement> entrada
                : listaCampeoes.entrySet()) {

            JsonObject resumo = entrada.getValue()
                    .getAsJsonObject();

            String campeaoId = resumo
                    .get("id")
                    .getAsString();

            String campeaoKey = resumo
                    .get("key")
                    .getAsString();

            Map<String, String> raridades
                    = buscarRaridadesCampeao(campeaoKey);

            contador++;

            System.out.println(
                    "[" + contador + "/" + listaCampeoes.size()
                    + "] Consultando: " + campeaoId
            );

            // Busca os dados completos nos dois idiomas
            JsonObject campeaoPt = buscarDadosCampeao(
                    versao,
                    campeaoId,
                    "pt_BR"
            );

            JsonObject campeaoEn = buscarDadosCampeao(
                    versao,
                    campeaoId,
                    "en_US"
            );

            // Adiciona as skins em português
            adicionarSkinsIdioma(
                    skinsPorApiId,
                    campeaoPt,
                    campeaoId,
                    "pt_BR",
                    raridades
            );

            // Adiciona os nomes em inglês
            adicionarSkinsIdioma(
                    skinsPorApiId,
                    campeaoEn,
                    campeaoId,
                    "en_US",
                    raridades
            );
        }

        // Converte o resultado para JsonArray
        JsonArray resultado = new JsonArray();

        for (JsonObject skin : skinsPorApiId.values()) {
            resultado.add(skin);
        }

        System.out.println(
                "Total de skins encontradas: " + resultado.size()
        );

        // Salva as informações no arquivo local
        return salvarSkinsDaApi(resultado);
    }

    // Mantém compatibilidade com chamadas antigas.
    // A lista recebida não limita mais a consulta.
    public JsonArray buscarSkins(java.util.List<String> nomesCadastrados)
            throws IOException, InterruptedException {

        return buscarSkins();
    }

    // =====================================================
    // BUSCAR VERSÃO DO DATA DRAGON
    // =====================================================
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

    // =====================================================
    // BUSCAR LISTA DE CAMPEÕES
    // =====================================================
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

    // =====================================================
    // BUSCAR DADOS COMPLETOS DE UM CAMPEÃO
    // =====================================================
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

    // =====================================================
    // ADICIONAR SKINS DE UM CAMPEÃO
  
        
private void adicionarSkinsIdioma(
        Map<String, JsonObject> resultado,
        JsonObject campeao,
        String campeaoId,
        String idioma,
        Map<String, String> raridades) {

    JsonArray skins = campeao.getAsJsonArray("skins");

    String nomeCampeao = campeao
            .get("name")
            .getAsString();

    for (int i = 0; i < skins.size(); i++) {

        JsonObject skin = skins.get(i).getAsJsonObject();

        if (!skin.has("id")
                || !skin.has("name")
                || !skin.has("num")) {

            continue;
        }

        // Ignora cromas
        if (skin.has("parentSkin")
                && !skin.get("parentSkin").isJsonNull()) {

            continue;
        }

        String nome = skin.get("name").getAsString();

        int numero = skin.get("num").getAsInt();

        // Ignora a skin padrão
        if (numero == 0
                || nome.equalsIgnoreCase("default")) {

            continue;
        }

        String apiId = skin.get("id").getAsString();

        // Verifica se a skin já foi adicionada
        JsonObject resultadoSkin = resultado.get(apiId);

        // PRIMEIRO: cria o objeto caso ainda não exista
        if (resultadoSkin == null) {

            resultadoSkin = new JsonObject();

            resultadoSkin.addProperty("apiId", apiId);

            resultadoSkin.addProperty(
                    "championId",
                    campeaoId
            );

            resultadoSkin.addProperty("num", numero);

            resultadoSkin.addProperty(
                    "displayIcon",
                    construirUrlImagem(campeaoId, numero)
            );

            resultado.put(apiId, resultadoSkin);
        }

        // DEPOIS: adiciona a raridade
        String raridade = raridades.get(apiId);

        if (raridade != null && !raridade.isBlank()) {

            resultadoSkin.addProperty(
                    "raridade",
                    raridade
            );
        }

        // Adiciona os nomes nos respectivos idiomas
        if (idioma.equals("pt_BR")) {

            resultadoSkin.addProperty(
                    "displayName",
                    nome
            );

            resultadoSkin.addProperty(
                    "championName",
                    nomeCampeao
            );

        } else {

            resultadoSkin.addProperty(
                    "displayNameEn",
                    nome
            );

            resultadoSkin.addProperty(
                    "championNameEn",
                    nomeCampeao
            );
        }
    }
}

    

    // =====================================================
    // CONSTRUIR URL DA IMAGEM
    // =====================================================
    private String construirUrlImagem(
            String campeaoId,
            int numero) {

        if (campeaoId.equalsIgnoreCase("Fiddlesticks")) {
            campeaoId = "FiddleSticks";
        }

        return URL_BASE
                + "/cdn/img/champion/splash/"
                + campeaoId + "_" + numero + ".jpg";
    }

    // =====================================================
    // SALVAR SKINS DA API
    // =====================================================
    private JsonArray salvarSkinsDaApi(JsonArray skinsApi)
            throws IOException {

        JsonArray existentes = carregarSkinsSalvas();

        Map<String, Integer> idsExistentes = new HashMap<>();

        JsonArray registrosManuais = new JsonArray();

        int maiorId = 0;

        // Analisa o arquivo existente
        for (int i = 0; i < existentes.size(); i++) {

            JsonObject skin = existentes.get(i).getAsJsonObject();

            int id = obterId(skin);

            if (id > maiorId) {
                maiorId = id;
            }

            // Possui apiId: veio da API
            if (skin.has("apiId")
                    && !skin.get("apiId").isJsonNull()
                    && !obterTexto(skin, "apiId").isEmpty()) {

                String apiId = obterTexto(skin, "apiId");

                if (id > 0) {
                    idsExistentes.put(apiId, id);
                }

            } else {

                // Não possui apiId: cadastro manual
                registrosManuais.add(skin.deepCopy());
            }
        }

        int proximoId = maiorId + 1;

        JsonArray arquivoAtualizado = new JsonArray();

        // Preserva os registros manuais
        for (int i = 0; i < registrosManuais.size(); i++) {

            JsonObject skin
                    = registrosManuais.get(i).getAsJsonObject();

            if (obterId(skin) <= 0) {
                skin.addProperty("id", proximoId++);
            }

            arquivoAtualizado.add(skin);
        }

        JsonArray skinsAtualizadas = new JsonArray();

        Set<String> idsProcessados = new HashSet<>();

        // Processa todas as skins recebidas
        for (int i = 0; i < skinsApi.size(); i++) {

            JsonObject skinApi = skinsApi.get(i).getAsJsonObject();

            String apiId = obterTexto(skinApi, "apiId");

            if (apiId.isEmpty()) {
                continue;
            }

            // Evita duplicações
            if (!idsProcessados.add(apiId)) {
                continue;
            }

            JsonObject skinSalva = skinApi.deepCopy();

            Integer idExistente = idsExistentes.get(apiId);

            if (idExistente != null) {

                // Mantém o ID local anterior
                skinSalva.addProperty("id", idExistente);

            } else {

                // Cria um ID novo
                skinSalva.addProperty("id", proximoId++);
            }

            arquivoAtualizado.add(skinSalva);
            skinsAtualizadas.add(skinSalva.deepCopy());
        }

        // Grava o arquivo atualizado
        gravarSkins(arquivoAtualizado);

        return skinsAtualizadas;
    }

    // =====================================================
    // CARREGAR ARQUIVO LOCAL
    // =====================================================
    private JsonArray carregarSkinsSalvas()
            throws IOException {

        if (!Files.exists(ARQUIVO_SKINS)) {
            return new JsonArray();
        }

        String conteudo = Files.readString(
                ARQUIVO_SKINS,
                StandardCharsets.UTF_8
        );

        if (conteudo.isBlank()) {
            return new JsonArray();
        }

        try {

            return JsonParser
                    .parseString(conteudo)
                    .getAsJsonArray();

        } catch (RuntimeException e) {

            throw new IOException(
                    "Erro ao ler arquivo de skins do LoL.",
                    e
            );
        }
    }

    // =====================================================
    // GRAVAR ARQUIVO LOCAL
    // =====================================================
    private void gravarSkins(JsonArray skins)
            throws IOException {

        Files.createDirectories(
                ARQUIVO_SKINS.getParent()
        );

        Files.writeString(
                ARQUIVO_SKINS,
                GSON.toJson(skins),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        );
    }

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================
    private int obterId(JsonObject objeto) {

        try {

            if (objeto.has("id")
                    && !objeto.get("id").isJsonNull()) {

                return objeto.get("id").getAsInt();
            }

        } catch (RuntimeException e) {
            return 0;
        }

        return 0;
    }

    private String obterTexto(JsonObject objeto, String campo) {

        if (objeto.has(campo)
                && !objeto.get(campo).isJsonNull()) {

            return objeto.get(campo).getAsString();
        }

        return "";
    }

    // =====================================================
    // REQUISIÇÃO HTTP
    // =====================================================
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
}
