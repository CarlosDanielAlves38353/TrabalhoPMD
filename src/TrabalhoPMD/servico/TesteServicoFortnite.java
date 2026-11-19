
package TrabalhoPMD.servico;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class TesteServicoFortnite {

    public static void main(String[] args) {

        ServicoFortnite servico = new ServicoFortnite();

        try {

            System.out.println(
                    "Iniciando importação do Fortnite..."
            );

            // Primeiro busca as skins
            JsonArray skins = servico.buscarSkins();

            System.out.println("==============================");
            System.out.println("IMPORTAÇÃO CONCLUÍDA!");
            System.out.println("==============================");

            System.out.println(
                    "Total de skins: " + skins.size()
            );

            System.out.println(
                    "Arquivo: dados/skins_fortnite.json"
            );

            // =====================================
            // DISTRIBUIÇÃO DAS SKINS
            // =====================================

            System.out.println(
                    "\n====== DISTRIBUIÇÃO DAS SKINS ======"
            );

            Map<String, Integer> distribuicao =
                    new TreeMap<>();

            for (int i = 0; i < skins.size(); i++) {

                JsonObject skin = skins.get(i).getAsJsonObject();

                String raridade = obterTexto(skin, "raridade");

                if (raridade.isBlank()) {
                    raridade = "Sem categoria";
                }

                distribuicao.put(
                        raridade,
                        distribuicao.getOrDefault(raridade, 0) + 1
                );
            }

            for (Map.Entry<String, Integer> entrada
                    : distribuicao.entrySet()) {

                System.out.println(
                        entrada.getKey() + ": " + entrada.getValue()
                );
            }

            // =====================================
            // EXEMPLOS DE SKINS
            // =====================================

            System.out.println(
                    "\n====== EXEMPLOS DE SKINS ======"
            );

            for (int i = 0; i < Math.min(15, skins.size()); i++) {

                JsonObject skin = skins.get(i).getAsJsonObject();

                System.out.println(
                        obterTexto(skin, "displayName")
                        + " | Raridade: "
                        + obterTexto(skin, "raridade")
                        + " | Base: "
                        + obterTexto(skin, "raridadeBase")
                        + " | Série: "
                        + obterTexto(skin, "series")
                );
            }

        } catch (IOException e) {

            System.err.println("Erro na importação:");

            e.printStackTrace();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.err.println(
                    "Importação interrompida."
            );
        }
    }

    // Evita erros quando algum campo não existe
    private static String obterTexto(
            JsonObject objeto,
            String campo) {

        if (objeto.has(campo)
                && !objeto.get(campo).isJsonNull()) {

            return objeto.get(campo).getAsString();
        }

        return "";
    }
}
