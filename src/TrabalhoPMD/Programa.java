package TrabalhoPMD;

import TrabalhoPMD.dados.BancoDados;
import TrabalhoPMD.visao.FramePrincipal;

import javax.swing.SwingUtilities;

public class Programa {

    public static void main(String[] args) {

        // Recupera os dados antes de abrir a interface
        BancoDados.getInstancia().recuperarTodos();

        SwingUtilities.invokeLater(() -> {
            FramePrincipal tela = new FramePrincipal();
            tela.setLocationRelativeTo(null);
            tela.setVisible(true);
        });
    }
}