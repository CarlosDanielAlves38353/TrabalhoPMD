
package TrabalhoPMD.modelo;

public class Skin extends Entidade {


    /*Alterar DPS */
    private static final long serialVersionUID = -4731270119128210323L; /*- preserva a compatibilidade com os dados serializados pela versão anterior dessa classe. */

    private String nome;
    private String jogo;
    private String raridade;
    private Usuario cadastradaPor;
    private String imagemUrl;

    // Construtor original
    public Skin(int id, String nome, String jogo, String raridade,
                Usuario cadastradaPor) {

        this(id, nome, jogo, raridade, cadastradaPor, null);
    }

    // Novo construtor com imagem
    public Skin(int id, String nome, String jogo, String raridade,
                Usuario cadastradaPor, String imagemUrl) {

        super(id);

        this.nome = nome;
        this.jogo = jogo;
        this.raridade = raridade;
        this.cadastradaPor = cadastradaPor;
        this.imagemUrl = imagemUrl;
    }

    // Getters e setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getJogo() {
        return jogo;
    }

    public void setJogo(String jogo) {
        this.jogo = jogo;
    }

    public String getRaridade() {
        return raridade;
    }

    public void setRaridade(String raridade) {
        this.raridade = raridade;
    }

    public Usuario getCadastradaPor() {
        return cadastradaPor;
    }

    public void setCadastradaPor(Usuario cadastradaPor) {
        this.cadastradaPor = cadastradaPor;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    @Override
    public String toString() {

        return String.format(
                "┌──────────────────────────────────────┐%n" +
                "│               SKIN                   │%n" +
                "├──────────────────────────────────────┤%n" +
                "│ ID: %-33d│%n" +
                "│ Nome: %-29s  │%n" +
                "│ Jogo: %-29s  │%n" +
                "│ Raridade: %-25s  │%n" +
                "│ Cadastrada por: %-18s    │%n" +
                "└──────────────────────────────────────┘",

                getId(),
                nome,
                jogo,
                raridade,
                cadastradaPor == null
                        ? "Nenhum"
                        : cadastradaPor.getNome()
        );
    }
}
