
package TrabalhoPMD.servico;

import TrabalhoPMD.dados.BancoDados;

public class TesteAtualizadorCS2 {

    public static void main(String[] args) {

        BancoDados.getInstancia().recuperarTodos();

        AtualizadorSkinsCS2 atualizador =
                new AtualizadorSkinsCS2();

        atualizador.atualizarImagens();
    }
}
