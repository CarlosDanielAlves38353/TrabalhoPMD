package TrabalhoPMD.servico;

import com.google.gson.JsonArray;
import java.io.IOException;

public class TesteServicoValorant {

    public static void main(String[] args) {

        ServicoValorant servico = new ServicoValorant();

        try {

            System.out.println("Buscando skins na API...");

            JsonArray skins = servico.buscarSkins();

            System.out.println("Consulta realizada!");

            System.out.println("Total de skins recebidas: "
                    + skins.size());

            System.out.println("Arquivo salvo em: "
                    + "dados/skins_valorant.json");

        } catch (IOException e) {

            System.err.println("Erro ao acessar ou salvar os dados:");
            e.printStackTrace();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.err.println("Consulta interrompida.");
        }
    }
}