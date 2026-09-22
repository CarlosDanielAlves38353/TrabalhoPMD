package TrabalhoPMD.modelo;

public class Avaliacao extends Entidade {

    private Usuario usuario;
    private Skin skin;
    private int nota;
    private String comentario;

  

    // Construtor completo
    public Avaliacao(int id, Usuario usuario, Skin skin,
                     int nota, String comentario) {

        super(id);

        this.usuario = usuario;
        this.skin = skin;
        this.nota = nota;
        this.comentario = comentario;
    }

    // Getters e setters

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "Avaliacao{id=" + getId()
                + ", usuario="
                + (usuario == null ? "nenhum" : usuario.getNome())
                + ", skin="
                + (skin == null ? "nenhuma" : skin.getNome())
                + ", nota=" + nota
                + ", comentario='" + comentario + '\''
                + '}';
    }
}