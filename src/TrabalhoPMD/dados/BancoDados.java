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

    private Map<Class<? extends Entidade>, EntidadeDAO<?>> daos;

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

    // Registra um DAO para cada entidade
    private <E extends Entidade> void registrar(Class<E> tipo) {

        daos.put(tipo, new EntidadeDAO<>(tipo));
    }

    // Retorna o DAO específico da entidade
    @SuppressWarnings("unchecked")
    public <E extends Entidade> EntidadeDAO<E> getDAO(Class<E> tipo) {

        EntidadeDAO<?> dao = daos.get(tipo);

        if (dao == null) {
            throw new IllegalArgumentException(
                    "DAO não encontrado para " + tipo.getSimpleName()
            );
        }

        return (EntidadeDAO<E>) dao;
    }

    // Recupera os dados de todas as entidades
    public void recuperarTodos() {

        for (EntidadeDAO<?> dao : daos.values()) {

            try {
                dao.recuperar();

            } catch (PersistenceException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    // Persiste os dados de todas as entidades
    public void persistirTodos() {

        for (EntidadeDAO<?> dao : daos.values()) {

            try {
                dao.persistir();

            } catch (PersistenceException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}