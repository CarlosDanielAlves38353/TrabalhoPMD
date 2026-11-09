
package TrabalhoPMD.servico;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TesteAPIValorant {

    public static void main(String[] args) {

        try {

            ServicoValorant servico = new ServicoValorant();

            System.out.println("Conectando à API do Valorant...");

            JsonArray skins = servico.buscarSkins();
            JsonArray categorias = servico.buscarCategorias();

System.out.println("\nTotal de categorias: " + categorias.size());

for (int i = 0; i < categorias.size(); i++) {

    JsonObject categoria = categorias.get(i).getAsJsonObject();

    System.out.println(
            categoria.get("uuid").getAsString()
            + " - "
            + categoria.get("displayName").getAsString()
    );
}

            System.out.println("Conexão realizada!");
            System.out.println("Total de skins recebidas: " + skins.size());

            if (!skins.isEmpty()) {

                JsonObject primeiraSkin = skins.get(0).getAsJsonObject();

                System.out.println(
                        "Primeira skin: "
                        + primeiraSkin.get("displayName").getAsString()
                );
                
                String raridade = servico.buscarNomeCategoria(
        categorias,
        primeiraSkin.get("contentTierUuid").getAsString()
);

System.out.println("Raridade: " + raridade);
                System.out.println("UUID: " + primeiraSkin.get("uuid"));
System.out.println("Nome: " + primeiraSkin.get("displayName"));
System.out.println("Imagem: " + primeiraSkin.get("displayIcon"));
System.out.println("Categoria: " + primeiraSkin.get("contentTierUuid"));
            }

        } catch (Exception e) {

            System.out.println("Não foi possível acessar a API.");
            e.printStackTrace();
        }
    }
}
