
package TrabalhoPMD.servico;

import com.google.gson.JsonArray;
import java.io.IOException;

public class TesteServicoLeagueOfLegends {

    public static void main(String[] args) {

        ServicoLeagueOfLegends servico =
                new ServicoLeagueOfLegends();

        try {

            System.out.println("Iniciando importação do LoL...");

            JsonArray skins = servico.buscarSkins();

            System.out.println("==============================");
            System.out.println("IMPORTAÇÃO CONCLUÍDA!");
            System.out.println("==============================");

            System.out.println(
                    "Total de skins: " + skins.size()
            );

            System.out.println(
                    "Arquivo: dados/skins_lol.json"
            );

        } catch (IOException e) {

            System.err.println("Erro na importação:");
            e.printStackTrace();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.err.println("Importação interrompida.");
        }
    }
}
