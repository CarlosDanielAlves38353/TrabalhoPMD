package pmd.modelo;

import java.util.ArrayList;
import java.util.List;

public class Colecao extends Entidade{

    private Usuario usuario;
    private String nome;
    private List<ItemColecao> itens;

    public Colecao(){
        super();
        iten = new ArrayList<>();
    }

    public Colecao(int id, Usuario usuario, String nome){

        super(id);
        this.usuario = usuario;
        this.nome = nome;
        this.itens= new ArrayList<>();
    }

    //Getter e Setter

    //Adicionar Skin na coleção
    public void adicionarItem(Skin skin, int quantidade){

        if(skin == null || quantidade<=0){
            return "Skin e quantidade devem ser validos."
        }

        for(ItemColecao item : itens){
            if(item.getSkin().getId() == skin.getId()){
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }

        item.add(new ItemColecao(skin, quantidade));
    }

    public boolean removerItem(int idSkin){
        for(int i=0;i<item.size();i++){
            if(item.get(i).getSkin().getId() == idSkin){
                item.remove(i);

                return true
            }
        }
        return false;
    }

    @Override
    public String toString(){
        return Colecao
    }
}