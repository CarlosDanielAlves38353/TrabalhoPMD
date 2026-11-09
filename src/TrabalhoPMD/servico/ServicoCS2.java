
package TrabalhoPMD.servico;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ServicoCS2 {

    private static final String API_URL =
            "https://raw.githubusercontent.com/ByMykel/CSGO-API/main/public/api/en/skins.json";

    private final HttpClient cliente;

    public ServicoCS2() {

        cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

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

        return JsonParser
                .parseString(resposta.body())
                .getAsJsonArray();
    }
}
