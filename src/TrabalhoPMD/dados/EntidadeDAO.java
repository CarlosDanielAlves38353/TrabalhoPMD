package TrabalhoPMD.dados;

import TrabalhoPMD.modelo.Entidade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class EntidadeDAO {

    private Set<Entidade> entidades;
    private final Class<? extends Entidade> tipo; // ? = Ela rejeitaria classes filhas da classe Entidade
    private final String arquivo;

    public EntidadeDAO(Class<? extends Entidade> tipo) {
        this.tipo = tipo;
        this.arquivo = "dados_" + tipo.getSimpleName() + ".txt";
        this.entidades = new HashSet<>();
    }

    // Salva uma entidade
    public boolean salvar(Entidade entidade) {

        try {

            if (entidade == null || !tipo.isInstance(entidade)) {
                return false;
            }

            // Verifica se já existe uma entidade com o mesmo ID
            if (buscar(entidade.getId()) != null) {
                return false;
            }

            return entidades.add(entidade);

        } catch (Exception e) {
            return false;
        }
    }

    // Atualiza uma entidade existente
    public boolean atualizar(Entidade entidade) {

        if (entidade == null || !tipo.isInstance(entidade)) {
            return false;
        }

        Entidade existente = buscar(entidade.getId());

        if (existente == null) {
            return false;
        }

        entidades.remove(existente);
        entidades.add(entidade);

        return true;
    }

    // Apaga uma entidade pelo ID
    public Entidade apagar(int id) {

        Entidade entidade = buscar(id);

        if (entidade != null) {
            entidades.remove(entidade);
        }

        return entidade;
    }

    // Busca uma entidade pelo ID
    public Entidade buscar(int id) {

        for (Entidade entidade : entidades) {

            if (entidade.getId() == id) {
                return entidade;
            }
        }

        return null;
    }

    // Retorna todas as entidades em um array ordenado por ID
    public Entidade[] carregar() {

        Entidade[] resultado =
                entidades.toArray(new Entidade[0]);

        Arrays.sort(
                resultado,
                Comparator.comparingInt(Entidade::getId)
        );

        return resultado;
    }

    // Salva o conjunto em um arquivo
    public boolean persistir() {

        try (
            ObjectOutputStream saida =
                    new ObjectOutputStream(
                            new FileOutputStream(arquivo)
                    )
        ) {

            saida.writeObject(entidades);

            return true;

        } catch (IOException e) {

            System.out.println(
                    "Erro ao persistir "
                    + tipo.getSimpleName()
                    + ": "
                    + e.getMessage()
            );

            return false;
        }
    }

    // Recupera o conjunto do arquivo
    public boolean recuperar() {

        File arquivoDados = new File(arquivo);

        if (!arquivoDados.exists()) {
            return false;
        }

        try (
            ObjectInputStream entrada =
                    new ObjectInputStream(
                            new FileInputStream(arquivoDados)
                    )
        ) {

            Object objeto = entrada.readObject();

            // Verifica se o objeto é uma coleção do tipo Set, independente do tipo dos elementos
            if (!(objeto instanceof Set<?>)) {
                return false;
            }

            Set<?> conjunto = (Set<?>) objeto;

            Set<Entidade> recuperado =
                    new HashSet<>();

            for (Object item : conjunto) {

                if (!tipo.isInstance(item)) {
                    return false;
                }

                recuperado.add((Entidade) item);
            }

            entidades = recuperado;

            return true;

        } catch (
                IOException |
                ClassNotFoundException e
        ) {

            System.out.println(
                    "Erro ao recuperar "
                    + tipo.getSimpleName()
                    + ": "
                    + e.getMessage()
            );

            return false;
        }
    }
}