package TrabalhoPMD.servico;

import TrabalhoPMD.dados.BancoDados;
import TrabalhoPMD.modelo.Caixa;
import TrabalhoPMD.modelo.Skin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TesteServicoCaixa {

    public static void main(String[] args) {

        BancoDados.getInstancia().recuperarTodos();

        ServicoCaixa servico = new ServicoCaixa();

        List<Caixa> caixas = servico.listarCaixas();

        for (Caixa caixa : caixas) {

            List<Skin> disponiveis = servico.listarSkins(caixa);

            System.out.println();
            System.out.println("========================================");
            System.out.println(caixa);
            System.out.println("Skins disponíveis: " + disponiveis.size());
            System.out.println("Probabilidades: " + caixa.getProbabilidades());

            if (disponiveis.isEmpty()) {
                System.out.println("Não há skins para testar esta caixa.");
                continue;
            }

            // Faz 10.000 sorteios sem alterar a coleção de nenhum usuário.
            Map<String, Integer> frequencias = new LinkedHashMap<>();

            for (String raridade : caixa.getProbabilidades().keySet()) {
                frequencias.put(raridade, 0);
            }

            int quantidadeTestes = 10000;

            for (int i = 0; i < quantidadeTestes; i++) {

                Skin sorteada = servico.sortearSkin(caixa);

                frequencias.put(
                        sorteada.getRaridade(),
                        frequencias.get(sorteada.getRaridade()) + 1
                );
            }

            System.out.println("Resultado de " + quantidadeTestes + " sorteios:");

            for (Map.Entry<String, Integer> entrada
                    : frequencias.entrySet()) {

                double percentual =
                        entrada.getValue() * 100.0 / quantidadeTestes;

                System.out.printf(
                        "%-18s %5.2f%% (%d)%n",
                        entrada.getKey(),
                        percentual,
                        entrada.getValue()
                );
            }

            Skin exemplo = servico.sortearSkin(caixa);

            System.out.println("Exemplo de recompensa: "
                    + exemplo.getNome()
                    + " [" + exemplo.getRaridade() + "]");
        }
    }
}
