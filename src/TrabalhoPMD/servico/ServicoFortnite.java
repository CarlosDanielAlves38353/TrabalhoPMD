
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
import java.util.Map;
import java.util.Set;

public class ServicoFortnite {

    private static final String API_URL =
            "https://fortnite-api.com/v2/cosmetics/br";

    private static final Path ARQUIVO_SKINS =
            Path.of("dados", "skins_fortnite.json");

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();

    private final HttpClient cliente;

    public ServicoFortnite() {

        cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    // =====================================================
    // BUSCAR SKINS
    // =====================================================

    public JsonArray buscarSkins()
            throws IOException, InterruptedException {

        return buscarSkins("pt-BR");
    }

    public JsonArray buscarSkins(String idioma)
            throws IOException, InterruptedException {

        System.out.println("Consultando API do Fortnite...");

        String url = API_URL + "?language=" + idioma;

        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(60))
                .GET()
                .build();

        HttpResponse<String> resposta = cliente.send(
                requisicao,
                HttpResponse.BodyHandlers.ofString()
        );

        if (resposta.statusCode() != 200) {

            throw new IOException(
                    "Erro na API do Fortnite: "
                    + resposta.statusCode()
            );
        }

        JsonObject json = JsonParser
                .parseString(resposta.body())
                .getAsJsonObject();

        JsonArray cosmeticos = json.getAsJsonArray("data");

        if (cosmeticos == null) {

            throw new IOException(
                    "A API não retornou a lista de cosméticos."
            );
        }

        // Filtra apenas as skins de personagens
        JsonArray skinsApi = filtrarSkins(cosmeticos);

        System.out.println(
                "Skins encontradas: " + skinsApi.size()
        );

        return salvarSkinsDaApi(skinsApi);
    }

    // =====================================================
    // FILTRAR SOMENTE OUTFITS
    // =====================================================

    private JsonArray filtrarSkins(JsonArray cosmeticos) {

        JsonArray resultado = new JsonArray();

        for (int i = 0; i < cosmeticos.size(); i++) {

            JsonObject cosmetico =
                    cosmeticos.get(i).getAsJsonObject();

            JsonObject tipo = obterObjeto(cosmetico, "type");

            String valor = obterTexto(tipo, "value");

            String display = obterTexto(tipo, "displayValue");

            String backend = obterTexto(tipo, "backendValue");

            // Somente personagens (Outfits)
            boolean ehOutfit =
                    valor.equalsIgnoreCase("outfit")
                    || display.equalsIgnoreCase("Outfit")
                    || backend.equalsIgnoreCase("AthenaCharacter");

            if (ehOutfit) {
                resultado.add(cosmetico.deepCopy());
            }
        }

        return resultado;
    }

    // =====================================================
    // CONVERTER COSMÉTICO PARA REGISTRO DO PROJETO
    // =====================================================

    private JsonObject converterSkin(JsonObject original) {

        JsonObject skin = new JsonObject();

        JsonObject imagens = obterObjeto(original, "images");

        JsonObject raridade = obterObjeto(original, "rarity");

        JsonObject serie = obterObjeto(original, "series");

        String apiId = obterTexto(original, "id");

        String nome = obterTexto(original, "name");

        String descricao = obterTexto(original, "description");

        String imagem = obterTexto(imagens, "icon");

        if (imagem.isEmpty()) {
            imagem = obterTexto(imagens, "smallIcon");
        }

        if (imagem.isEmpty()) {
            imagem = obterTexto(imagens, "featured");
        }

        String raridadeBase = obterTexto(
                raridade,
                "displayValue"
        );

        if (raridadeBase.isEmpty()) {
            raridadeBase = obterTexto(raridade, "value");
        }

        String serieValor = obterTexto(serie, "value");

        String categoria = identificarCategoria(
                raridadeBase,
                serieValor
        );

        skin.addProperty("apiId", apiId);
        skin.addProperty("displayName", nome);
        skin.addProperty("description", descricao);
        skin.addProperty("displayIcon", imagem);

        skin.addProperty("raridade", categoria);
        skin.addProperty("raridadeBase", raridadeBase);
        skin.addProperty("series", serieValor);

        return skin;
    }

    // =====================================================
    // IDENTIFICAR CATEGORIA
    // =====================================================

    private String identificarCategoria(
            String raridade,
            String serie) {

        String serieNormalizada = serie.toLowerCase();

        // Icon Series
        if (serieNormalizada.contains("creatorcollab")
                || serieNormalizada.contains("icon")) {

            return "Icon Series";
        }

        // Gaming Legends
        if (serieNormalizada.contains("columbus")
                || serieNormalizada.contains("gaminglegend")) {

            return "Gaming Legends";
        }

        // Marvel
        if (serieNormalizada.contains("marvel")) {
            return "Marvel";
        }

        // DC
        if (serieNormalizada.contains("dcu")
                || serieNormalizada.equals("dcseries")) {

            return "DC";
        }

        // Quando não pertence a uma série especial,
        // utiliza a raridade original.
        if (raridade == null || raridade.isBlank()) {
            return "Sem categoria";
        }

        String valor = raridade.toLowerCase();

        if (valor.equals("rare")) {
            return "Rare";
        }

        if (valor.equals("epic")) {
            return "Epic";
        }

        if (valor.equals("legendary")) {
            return "Legendary";
        }

        return raridade;
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

        // Verifica os registros existentes
        for (int i = 0; i < existentes.size(); i++) {

            JsonObject skin =
                    existentes.get(i).getAsJsonObject();

            int id = obterId(skin);

            if (id > maiorId) {
                maiorId = id;
            }

            // Possui apiId: registro da API
            if (skin.has("apiId")
                    && !skin.get("apiId").isJsonNull()) {

                String apiId = obterTexto(skin, "apiId");

                if (!apiId.isEmpty() && id > 0) {
                    idsExistentes.put(apiId, id);
                }

            } else {

                // Sem apiId: cadastro manual
                registrosManuais.add(skin.deepCopy());
            }
        }

        int proximoId = maiorId + 1;

        JsonArray arquivoAtualizado = new JsonArray();

        // Preserva os registros manuais
        for (int i = 0; i < registrosManuais.size(); i++) {

            JsonObject skin =
                    registrosManuais.get(i).getAsJsonObject();

            if (obterId(skin) <= 0) {
                skin.addProperty("id", proximoId++);
            }

            arquivoAtualizado.add(skin);
        }

        JsonArray skinsAtualizadas = new JsonArray();

        Set<String> idsProcessados = new HashSet<>();

        // Processa as skins da API
        for (int i = 0; i < skinsApi.size(); i++) {

            JsonObject original =
                    skinsApi.get(i).getAsJsonObject();

            String apiId = obterTexto(original, "id");

            if (apiId.isEmpty()
                    || !idsProcessados.add(apiId)) {

                continue;
            }

            JsonObject skinSalva = converterSkin(original);

            Integer idExistente = idsExistentes.get(apiId);

            if (idExistente != null) {

                // Mantém o ID local anterior
                skinSalva.addProperty("id", idExistente);

            } else {

                // Cria um ID local novo
                skinSalva.addProperty("id", proximoId++);
            }

            arquivoAtualizado.add(skinSalva);

            skinsAtualizadas.add(skinSalva.deepCopy());
        }

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
                    "Erro ao ler arquivo de skins do Fortnite.",
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

    private JsonObject obterObjeto(
            JsonObject objeto,
            String campo) {

        if (objeto.has(campo)
                && objeto.get(campo).isJsonObject()) {

            return objeto.getAsJsonObject(campo);
        }

        return new JsonObject();
    }

    private String obterTexto(
            JsonObject objeto,
            String campo) {

        if (objeto != null
                && objeto.has(campo)
                && !objeto.get(campo).isJsonNull()) {

            return objeto.get(campo).getAsString();
        }

        return "";
    }

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
}
