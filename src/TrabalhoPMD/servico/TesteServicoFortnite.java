
package TrabalhoPMD.servico;

import com.google.gson.JsonArray;

import java.io.IOException;

public class TesteServicoFortnite {

    public static void main(String[] args) {

        ServicoFortnite servico = new ServicoFortnite();

        try {

            System.out.println(
                    "Iniciando importação do Fortnite..."
            );

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
}
