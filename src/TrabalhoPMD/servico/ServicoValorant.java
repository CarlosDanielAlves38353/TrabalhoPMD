package TrabalhoPMD.servico;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServicoValorant {

    private static final String API_URL
            = "https://valorant-api.com/v1/weapons/skins";

    private static final String CATEGORIAS_URL
            = "https://valorant-api.com/v1/contenttiers";

    // Arquivo local que armazenará as skins
    private static final Path ARQUIVO_SKINS
            = Path.of("dados", "skins_valorant.json");

    private static final Gson GSON
            = new GsonBuilder().setPrettyPrinting().create();

    private final HttpClient cliente;

    public ServicoValorant() {

        cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    // =====================================================
    // BUSCAR SKINS NA API
    // =====================================================
    public JsonArray buscarSkins()
            throws IOException, InterruptedException {

        return buscarSkins("pt-BR");
    }

    public JsonArray buscarSkins(String idioma)
            throws IOException, InterruptedException {

        String url = API_URL + "?language=" + idioma;

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
                    "Erro na API do Valorant: "
                    + resposta.statusCode()
            );
        }

        JsonObject json = JsonParser
                .parseString(resposta.body())
                .getAsJsonObject();

        JsonArray skinsApi = json.getAsJsonArray("data");

        // Salva os dados e retorna as skins com seus IDs locais
        return salvarSkinsDaApi(skinsApi);
    }

    // =====================================================
    // SALVAR SKINS RECEBIDAS DA API
    // =====================================================
    private JsonArray salvarSkinsDaApi(JsonArray skinsApi)
            throws IOException {

        JsonArray existentes = carregarSkinsSalvas();

        // Relaciona o UUID da API ao ID que já existia
        Map<String, Integer> idsExistentes = new HashMap<>();

        // Registros que não vieram da API serão preservados
        JsonArray registrosManuais = new JsonArray();

        int maiorId = 0;

        // Analisa o arquivo atual
        for (int i = 0; i < existentes.size(); i++) {

            JsonObject skin = existentes.get(i).getAsJsonObject();

            int id = obterId(skin);

            if (id > maiorId) {
                maiorId = id;
            }

            // Se possui UUID, veio da API
            if (skin.has("uuid") && !skin.get("uuid").isJsonNull()) {

                String uuid = obterTexto(skin, "uuid");

                if (!uuid.isEmpty() && id > 0) {
                    idsExistentes.put(uuid, id);
                }

            } else {

                // Se não possui UUID, é um cadastro manual
                registrosManuais.add(skin.deepCopy());
            }
        }

        int proximoId = maiorId + 1;

        // Monta o arquivo atualizado, preservando os registros manuais
        JsonArray arquivoAtualizado = new JsonArray();

        for (int i = 0; i < registrosManuais.size(); i++) {

            JsonObject skin = registrosManuais.get(i).getAsJsonObject();

            // Garante que registros antigos também tenham ID
            if (obterId(skin) <= 0) {
                skin.addProperty("id", proximoId++);
            }

            if (!skin.has("origem")) {
                skin.addProperty("origem", "MANUAL");
            }

            arquivoAtualizado.add(skin);
        }

        // Evita duplicar skins que tenham o mesmo UUID
        Set<String> uuidsProcessados = new HashSet<>();

        // Guarda somente as skins atualizadas da API para retornar
        JsonArray skinsAtualizadas = new JsonArray();

        for (int i = 0; i < skinsApi.size(); i++) {

            JsonObject skinApi = skinsApi.get(i).getAsJsonObject();

            String uuid = obterTexto(skinApi, "uuid");

            if (uuid.isEmpty()) {
                continue;
            }

            if (!uuidsProcessados.add(uuid)) {
                continue;
            }

            JsonObject skinSalva = skinApi.deepCopy();

            // Verifica se essa skin já foi salva anteriormente
            Integer idExistente = idsExistentes.get(uuid);

            if (idExistente != null) {

                // Mantém o ID anterior
                skinSalva.addProperty("id", idExistente);

            } else {

                // Cria um novo ID para uma skin inédita
                skinSalva.addProperty("id", proximoId++);
            }


            arquivoAtualizado.add(skinSalva);
            skinsAtualizadas.add(skinSalva.deepCopy());
        }

        // Grava tudo no arquivo JSON
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
                    "Não foi possível ler o arquivo de skins.",
                    e
            );
        }
    }

    // =====================================================
    // GRAVAR ARQUIVO LOCAL
    // =====================================================
    private void gravarSkins(JsonArray skins)
            throws IOException {

        // Cria a pasta caso ainda não exista
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

            if (objeto.has("id") && !objeto.get("id").isJsonNull()) {
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
    // BUSCAR CATEGORIAS
    // =====================================================
    public JsonArray buscarCategorias()
            throws IOException, InterruptedException {

        String url = CATEGORIAS_URL + "?language=pt-BR";

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
                    "Erro ao buscar categorias. Código: "
                    + resposta.statusCode()
            );
        }

        JsonObject json = JsonParser
                .parseString(resposta.body())
                .getAsJsonObject();

        return json.getAsJsonArray("data");
    }

    // =====================================================
    // BUSCAR NOME DA CATEGORIA
    // =====================================================
    public String buscarNomeCategoria(JsonArray categorias, String uuid) {

        for (int i = 0; i < categorias.size(); i++) {

            JsonObject categoria
                    = categorias.get(i).getAsJsonObject();

            String uuidCategoria
                    = obterTexto(categoria, "uuid");

            if (uuidCategoria.equals(uuid)) {

                return obterTexto(categoria, "displayName");
            }
        }

        return "Sem categoria";
    }
}
