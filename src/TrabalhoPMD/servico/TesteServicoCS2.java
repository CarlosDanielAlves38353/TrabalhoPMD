package TrabalhoPMD.servico;

import com.google.gson.JsonArray;
import java.io.IOException;

public class TesteServicoCS2 {

    public static void main(String[] args) {

        ServicoCS2 servico = new ServicoCS2();

        try {

            System.out.println("Buscando skins do CS2...");

            JsonArray skins = servico.buscarSkins();

            System.out.println("Consulta realizada!");

            System.out.println("Total de skins: " + skins.size());

            System.out.println(
                    "Arquivo salvo: dados/skins_cs2.json"
            );

        } catch (IOException e) {

            System.err.println("Erro ao acessar ou salvar:");
            e.printStackTrace();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.err.println("Consulta interrompida.");
        }
    }
}