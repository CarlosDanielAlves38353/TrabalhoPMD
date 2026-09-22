package TrabalhoPMD.modelo;

import java.util.ArrayList;
import java.util.List;

public class Colecao extends Entidade {

    private Usuario usuario;
    private String nome;
    private List<ItemColecao> itens;

    // Construtor vazio
    public Colecao(int id) {
        super(id);
        itens = new ArrayList<>();
    }

    // Construtor completo
    public Colecao(int id, Usuario usuario, String nome) {
        super(id);

        this.usuario = usuario;
        this.nome = nome;
        this.itens = new ArrayList<>();
    }

    // Getters e setters

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ItemColecao> getItens() {
        return itens;
    }

    // Adiciona uma skin na coleção
    public void adicionarItem(Skin skin, int quantidade) {

        if (skin == null || quantidade <= 0) {
            throw new IllegalArgumentException(
                    "Skin e quantidade devem ser validos."
            );
        }

        for (ItemColecao item : itens) {

            if (item.getSkin().getId() == skin.getId()) {

                item.setQuantidade(
                        item.getQuantidade() + quantidade
                );

                return;
            }
        }

        itens.add(new ItemColecao(skin, quantidade));
    }

    // Remove uma skin da coleção
    public boolean removerItem(int idSkin) {

        for (int i = 0; i < itens.size(); i++) {

            if (itens.get(i).getSkin().getId() == idSkin) {

                itens.remove(i);

                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Colecao{id=" + getId()
                + ", nome='" + nome + '\''
                + ", usuario="
                + (usuario == null
                    ? "nenhum"
                    : usuario.getNome())
                + ", quantidadeDeSkins="
                + itens.size()
                + '}';
    }
}