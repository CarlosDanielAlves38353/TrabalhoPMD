package TrabalhoPMD.dados;

import TrabalhoPMD.modelo.Avaliacao;
import TrabalhoPMD.modelo.Colecao;
import TrabalhoPMD.modelo.Entidade;
import TrabalhoPMD.modelo.Skin;
import TrabalhoPMD.modelo.Usuario;

import java.util.HashMap;
import java.util.Map;

public class BancoDados {

    private static BancoDados instancia;

    private Map<Class<? extends Entidade>, EntidadeDAO> daos;

    // Construtor privado
    private BancoDados() {

        daos = new HashMap<>();

        registrar(Usuario.class);
        registrar(Skin.class);
        registrar(Avaliacao.class);
        registrar(Colecao.class);
    }

    // Retorna a única instância de BancoDados
    public static BancoDados getInstancia() {

        if (instancia == null) {
            instancia = new BancoDados();
        }

        return instancia;
    }

    // Cria e registra um DAO para uma entidade
    private void registrar(Class<? extends Entidade> tipo) {

        daos.put(
                tipo,
                new EntidadeDAO(tipo)
        );
    }

    // Retorna o DAO da entidade solicitada
    public EntidadeDAO getDAO(
            Class<? extends Entidade> tipo) {

        return daos.get(tipo);
    }

    // Recupera os dados de todas as entidades
    public void recuperarTodos() {

        for (EntidadeDAO dao : daos.values()) {
            dao.recuperar();
        }
    }

    // Persiste os dados de todas as entidades
    public void persistirTodos() {

        for (EntidadeDAO dao : daos.values()) {
            dao.persistir();
        }
    }
}