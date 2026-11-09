
package TrabalhoPMD.servico;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TesteServicoCS2 {

    public static void main(String[] args) {

        try {

            ServicoCS2 servico = new ServicoCS2();

            System.out.println("Consultando API do CS2...");

            JsonArray skins = servico.buscarSkins();

            System.out.println(
                    "Total de skins retornadas: " + skins.size()
            );

            System.out.println("\n===== PRIMEIRAS SKINS =====");

            for (int i = 0; i < Math.min(10, skins.size()); i++) {

                JsonObject skin = skins.get(i).getAsJsonObject();

                String nome = skin.get("name").getAsString();

                String imagem = skin.get("image").getAsString();

                System.out.println("\nNome: " + nome);
                System.out.println("Imagem: " + imagem);
            }

        } catch (Exception e) {

            System.out.println("Erro ao consultar API do CS2.");
            e.printStackTrace();
        }
    }
}
