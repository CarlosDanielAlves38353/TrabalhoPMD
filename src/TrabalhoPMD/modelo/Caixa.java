package TrabalhoPMD.modelo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Caixa {

    private final String nome;
    private final String jogo;
    private final Map<String, Double> probabilidades;

    public Caixa(
            String nome,
            String jogo,
            Map<String, Double> probabilidades) {

        if (nome == null || nome.trim().isEmpty()
                || jogo == null || jogo.trim().isEmpty()
                || probabilidades == null || probabilidades.isEmpty()) {

            throw new IllegalArgumentException(
                    "Nome, jogo e probabilidades devem ser informados.");
        }

        double total = 0;

        for (Map.Entry<String, Double> entrada : probabilidades.entrySet()) {

            if (entrada.getKey() == null
                    || entrada.getValue() == null
                    || entrada.getValue() <= 0) {

                throw new IllegalArgumentException(
                        "As probabilidades devem ser positivas.");
            }

            total += entrada.getValue();
        }

        if (Math.abs(total - 100.0) > 0.0001) {

            throw new IllegalArgumentException(
                    "A soma das probabilidades deve ser 100%.");
        }

        this.nome = nome;
        this.jogo = jogo;
        this.probabilidades = Collections.unmodifiableMap(
                new LinkedHashMap<>(probabilidades));
    }

    public String getNome() {
        return nome;
    }

    public String getJogo() {
        return jogo;
    }

    public Map<String, Double> getProbabilidades() {
        return probabilidades;
    }

    @Override
    public String toString() {
        return nome + " - " + jogo;
    }
}
