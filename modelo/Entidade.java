package pmd.modelo;

import java.io.Serializable;

public abstract class Entidade implements Serializable{

    private int id;

    public Entidade(){
    }

    public Entidade(id){
        this.id=id;
    }

    //Getter
    public int getId(){
        return id;
    }

    //Setter
    public void setId(int Id){
        this.id=id;
    }

    //toString
    @Override
    public String toString(){
        return "Entidade{id=" + id +"}";
    }
}