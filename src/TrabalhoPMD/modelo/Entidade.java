package TrabalhoPMD.modelo;

import java.io.Serializable;

public abstract class Entidade implements Serializable{

    private int id;

    public Entidade(int id){
        this.id=id;
    }

    //Getter
    public int getId(){
        return id;
    }

    //Setter
    public void setId(int Id){
        this.id = Id;
    }

    //toString
    @Override
    public String toString(){
        return "Entidade{id=" + id +"}";
    }
}