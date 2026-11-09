package TrabalhoPMD.servico;

import TrabalhoPMD.dados.BancoDados;

public class TesteAtualizadorLeagueOfLegends {

    public static void main(String[] args) {

        BancoDados.getInstancia().recuperarTodos();

        AtualizadorSkinsLeagueOfLegends atualizador =
                new AtualizadorSkinsLeagueOfLegends();

        atualizador.atualizarImagens();
    }
}