package TrabalhoPMD.modelo;

import java.io.Serializable;

public class ItemColecao implements Serializable {

    private Skin skin;
    private int quantidade;

    // Construtor
    public ItemColecao(Skin skin, int quantidade) {
        this.skin = skin;
        this.quantidade = quantidade;
    }

    // Getter e setter da skin

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    // Getter e setter da quantidade

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ItemColecao{skin="
                + (skin == null ? "nenhuma" : skin.getNome())
                + ", quantidade=" + quantidade
                + "}";
    }
}