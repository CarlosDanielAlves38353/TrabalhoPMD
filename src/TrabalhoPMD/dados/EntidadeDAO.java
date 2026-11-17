package TrabalhoPMD.dados;

import TrabalhoPMD.modelo.Entidade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EntidadeDAO<E extends Entidade> {

    private Set<E> entidades;
    private final Class<E> tipo;
    private final String arquivo;

    public EntidadeDAO(Class<E> tipo) {

        this.tipo = tipo;
        this.arquivo = "dados_" + tipo.getSimpleName() + ".txt";
        this.entidades = new HashSet<>();
    }

    // Procura uma entidade pelo ID sem lançar exceção
    private E encontrarPorId(int id) {

        for (E entidade : entidades) {

            if (entidade.getId() == id) {
                return entidade;
            }
        }

        return null;
    }

    // Salva uma entidade no conjunto
    public void salvar(E entidade) throws PersistenceException {

        if (entidade == null || !tipo.isInstance(entidade)) {

            throw new PersistenceException(
                    "SALVAR",
                    "Entidade inválida.",
                    entidade
            );
        }

        if (encontrarPorId(entidade.getId()) != null) {

            throw new PersistenceException(
                    "SALVAR",
                    "Já existe uma entidade com esse ID.",
                    entidade
            );
        }

        if (!entidades.add(entidade)) {

            throw new PersistenceException(
                    "SALVAR",
                    "Não foi possível adicionar a entidade.",
                    entidade
            );
        }
        persistir();
    }

    // Atualiza uma entidade existente pelo ID
    public void atualizar(E entidade) throws PersistenceException {

        if (entidade == null || !tipo.isInstance(entidade)) {

            throw new PersistenceException(
                    "ATUALIZAR",
                    "Entidade inválida.",
                    entidade
            );
        }

        E existente = encontrarPorId(entidade.getId());

        if (existente == null) {

            throw new PersistenceException(
                    "ATUALIZAR",
                    "Não existe entidade com esse ID.",
                    entidade.getId()
            );
        }

        entidades.remove(existente);
        entidades.add(entidade);
        persistir();
    }

    // Apaga uma entidade pelo ID
    public E apagar(int id) throws PersistenceException {

        E entidade = encontrarPorId(id);

        if (entidade == null) {

            throw new PersistenceException(
                    "APAGAR",
                    "Não existe entidade com esse ID.",
                    id
            );
        }

        entidades.remove(entidade);
        persistir();
        return entidade;
    }

    // Busca uma entidade pelo ID
    public E buscar(int id) throws PersistenceException {

        E entidade = encontrarPorId(id);

        if (entidade == null) {

            throw new PersistenceException(
                    "BUSCAR",
                    "Não existe entidade com esse ID.",
                    id
            );
        }

        return entidade;
    }

    // Retorna todas as entidades ordenadas pelo ID
    public E[] carregar() throws PersistenceException {

        if (entidades.isEmpty()) {

            throw new PersistenceException(
                    "CARREGAR TODOS",
                    "O conjunto está vazio.",
                    tipo.getSimpleName()
            );
        }

        E[] resultado = (E[]) Array.newInstance(
                tipo,
                entidades.size()
        );

        entidades.toArray(resultado);

        Arrays.sort(resultado, (a, b)
                -> a.compareTo(b)
        );

        return resultado;
    }

    // Persiste o conjunto em um arquivo
    public void persistir() throws PersistenceException {

        File arquivoDados = new File(arquivo);

        System.out.println(
                "[PERSISTENCIA] Salvando em: "
                + arquivoDados.getAbsolutePath()
        );

        try (ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(arquivoDados))) {

            saida.writeObject(entidades);
            System.out.println("[PERSISTENCIA] Arquivo salvo com sucesso!");
        } catch (IOException e) {

            throw new PersistenceException(
                    "PERSISTIR",
                    "Não foi possível gravar o arquivo: " + e.getMessage(),
                    arquivo,
                    e
            );
        }
    }

    // Recupera o conjunto armazenado no arquivo
    public void recuperar() throws PersistenceException {

        File arquivoDados = new File(arquivo);

        // Na primeira execução, ainda não existe arquivo.
        if (!arquivoDados.exists()) {
            entidades.clear();
            return;
        }

        try (
                ObjectInputStream entrada
                = new ObjectInputStream(
                        new FileInputStream(arquivoDados)
                )) {

            Object objeto = entrada.readObject();

            if (!(objeto instanceof Set<?>)) {

                throw new PersistenceException(
                        "RECUPERAR",
                        "O arquivo não contém um conjunto válido.",
                        arquivo
                );
            }

            Set<?> conjunto = (Set<?>) objeto;

            Set<E> recuperado = new HashSet<>();

            for (Object item : conjunto) {

                if (!tipo.isInstance(item)) {

                    throw new PersistenceException(
                            "RECUPERAR",
                            "O arquivo contém uma entidade incompatível.",
                            item
                    );
                }

                recuperado.add(tipo.cast(item));
            }

            entidades = recuperado;

        } catch (IOException | ClassNotFoundException e) {

            throw new PersistenceException(
                    "RECUPERAR",
                    "Não foi possível recuperar os dados: " + e.getMessage(),
                    arquivo,
                    e
            );
        }
    }
}
