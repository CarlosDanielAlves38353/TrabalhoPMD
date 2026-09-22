package TrabalhoPMD.modelo;

public class Usuario extends Entidade {
    private String nome;
    private String email;
    private String senha;
    private Perfil perfil;
    private Colecao colecao;

    public Usuario(int id) {
        super(id);
        this.perfil = Perfil.USUARIO;
    }

    public Usuario(int id, String nome, String email, String senha) {
        super(id);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = Perfil.USUARIO;
    }

    public Usuario(int id, String nome, String email, String senha, Perfil perfil) {
        super(id);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Colecao getColecao() {
        return colecao;
    }

    public void setColecao(Colecao colecao) {
        this.colecao = colecao;
    }

    @Override
    public String toString() {
        return "Usuario{id=" + getId()
                + ", nome='" + nome + '\''
                + ", email='" + email + '\''
                + ", perfil=" + perfil
                + '}';
    }
}