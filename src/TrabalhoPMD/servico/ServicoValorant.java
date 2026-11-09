
package TrabalhoPMD.servico;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ServicoValorant {

    private static final String API_URL =
"https://valorant-api.com/v1/weapons/skins";
    private final HttpClient cliente;

    public ServicoValorant() {
        cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

  
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

    return json.getAsJsonArray("data");
}
         
    
    
public JsonArray buscarCategorias()
        throws IOException, InterruptedException {

    String url = "https://valorant-api.com/v1/contenttiers?language=pt-BR";

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

public String buscarNomeCategoria(JsonArray categorias, String uuid) {

    for (int i = 0; i < categorias.size(); i++) {

        JsonObject categoria = categorias.get(i).getAsJsonObject();

        String uuidCategoria = categoria.get("uuid").getAsString();

        if (uuidCategoria.equals(uuid)) {
            return categoria.get("displayName").getAsString();
        }
    }

    return "Sem categoria";
}

}
