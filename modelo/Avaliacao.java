package pmd.modelo;

public class Avaliacao extends Entidade{

    private Usuario usuario;
    private Skin skin;
    private int nota;
    private String comentario;

    //Construtor vazio
    public Avaliacao(){
        super();
    }

    public Avaliacao(int id, Usuario usuario, Skin skin, int nota, String comentario){
        super(id);
        this.usuario= usuario;
        this.skin = skin;
        this.nota = nota;
        this.comentario = comentario;  
    }

    //Getter e Setter
}