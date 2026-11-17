package TrabalhoPMD.modelo;

public class TesteEntidade {

    public static void main(String[] args) {

        Skin skin1 = new Skin(10, "AK-47", "CS2", "Rara", null);
        Skin skin2 = new Skin(10, "AWP", "CS2", "Lendária", null);
        Skin skin3 = new Skin(20, "Vandal", "Valorant", "Premium", null);

        System.out.println(skin1.equals(skin2)); // true
        System.out.println(skin1.equals(skin3)); // false

        System.out.println(skin1.compareTo(skin2)); // 0
        System.out.println(skin1.compareTo(skin3)); // negativo
    }
}