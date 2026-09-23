package TrabalhoPMD.modelo;

import java.io.Serializable;

public class ItemColecao implements Serializable {

    private static final long serialVersionUID = 1L;

    private Skin skin;
    private int quantidade;

    public ItemColecao(Skin skin, int quantidade) {
        this.skin = skin;
        this.quantidade = quantidade;
    }

    public Skin getSkin() {
        return skin;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return String.format(
                "Skin: %-30s | Quantidade: %d",
                skin == null ? "Nenhuma" : skin.getNome(),
                quantidade
        );
    }
}