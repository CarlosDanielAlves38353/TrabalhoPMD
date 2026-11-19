
package TrabalhoPMD.servico;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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

public class ServicoCS2 {

    private static final String API_URL =
            "https://raw.githubusercontent.com/ByMykel/CSGO-API/main/public/api/en/skins.json";

    private static final Path ARQUIVO_SKINS =
            Path.of("dados", "skins_cs2.json");

    private static final Gson GSON =
            new GsonBuilder().setPrettyPrinting().create();

    private final HttpClient cliente;

    public ServicoCS2() {

        cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    // =====================================================
    // BUSCAR SKINS NA API
    // =====================================================

    public JsonArray buscarSkins()
            throws IOException, InterruptedException {

        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<String> resposta = cliente.send(
                requisicao,
                HttpResponse.BodyHandlers.ofString()
        );

        if (resposta.statusCode() != 200) {

            throw new IOException(
                    "Erro na API do CS2: "
                    + resposta.statusCode()
            );
        }

        JsonArray skinsApi = JsonParser
                .parseString(resposta.body())
                .getAsJsonArray();

        return salvarSkinsDaApi(skinsApi);
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

        // Identifica os registros já existentes
        for (int i = 0; i < existentes.size(); i++) {

            JsonObject skin = existentes.get(i).getAsJsonObject();

            int id = obterId(skin);

            if (id > maiorId) {
                maiorId = id;
            }

            // Possui apiId: veio da API
            if (skin.has("apiId")
                    && !skin.get("apiId").isJsonNull()) {

                String apiId = obterTexto(skin, "apiId");

                if (!apiId.isEmpty() && id > 0) {
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

            JsonObject skin =
                    registrosManuais.get(i).getAsJsonObject();

            if (obterId(skin) <= 0) {
                skin.addProperty("id", proximoId++);
            }

            arquivoAtualizado.add(skin);
        }

        JsonArray skinsAtualizadas = new JsonArray();

        Set<String> idsProcessados = new HashSet<>();

        // Processa as skins recebidas da API
        for (int i = 0; i < skinsApi.size(); i++) {

            JsonObject skinApi =
                    skinsApi.get(i).getAsJsonObject();

            // ID original fornecido pela API
            String apiId = obterTexto(skinApi, "id");

            if (apiId.isEmpty()) {
                continue;
            }

            if (!idsProcessados.add(apiId)) {
                continue;
            }

            JsonObject skinSalva = skinApi.deepCopy();

            Integer idExistente = idsExistentes.get(apiId);

            if (idExistente != null) {

                // Mantém o ID local anterior
                skinSalva.addProperty("id", idExistente);

            } else {

                // Gera um novo ID local
                skinSalva.addProperty("id", proximoId++);
            }

            // Preserva o identificador original da API
            skinSalva.addProperty("apiId", apiId);

            arquivoAtualizado.add(skinSalva);
            skinsAtualizadas.add(skinSalva.deepCopy());
        }

        gravarSkins(arquivoAtualizado);

        return skinsAtualizadas;
    }

    // =====================================================
    // CARREGAR ARQUIVO
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
                    "Erro ao ler arquivo de skins do CS2.",
                    e
            );
        }
    }

    // =====================================================
    // GRAVAR ARQUIVO
    // =====================================================

    private void gravarSkins(JsonArray skins)
            throws IOException {

        Files.createDirectories(ARQUIVO_SKINS.getParent());

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
}
