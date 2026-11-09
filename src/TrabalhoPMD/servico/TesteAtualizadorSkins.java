package TrabalhoPMD.servico;

import TrabalhoPMD.dados.BancoDados;

public class TesteAtualizadorSkins {

    public static void main(String[] args) {

        BancoDados.getInstancia().recuperarTodos();

        AtualizadorSkinsValorant atualizador =
                new AtualizadorSkinsValorant();

        atualizador.atualizarImagens();
    }
}