package TrabalhoPMD.modelo;

import java.io.Serializable;

public abstract class Entidade implements Serializable, Comparable<Entidade> {

    private int id;

    public Entidade(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Compara duas entidades pelo ID
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Entidade)) {
            return false;
        }

        Entidade outra = (Entidade) obj;

        return this.id == outra.id;
    }

    // Gera o hash com base no ID
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    // Compara duas entidades pelo ID
    @Override
    public int compareTo(Entidade outra) {
        return Integer.compare(this.id, outra.id);
    }

    // toString
    @Override
    public String toString() {
        return "Entidade{id=" + id + "}";
    }
}